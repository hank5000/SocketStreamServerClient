package com.example.hankwu.csilanstreamout;

import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/**
 * Created by charleswu-bc on 2015/11/26.
 */
public class WindowSurfaceFactory implements GLSurfaceView.EGLWindowSurfaceFactory {

    private EGLSurface mEGLPreviewSurface;
    private EGL10 egl10 = (EGL10) EGLContext.getEGL();

    private EGLSurface mRecordEGLSurface = null;


    private final String TAG = this.getClass().getName();


    EGLDisplay gDisplay = null;
    EGLConfig gConfig = null;

    public EGLSurface createWindowSurface(EGL10 egl, EGLDisplay display,
                                          EGLConfig config, Object nativeWindow) {
        EGLSurface result = null;
        try {
            mEGLPreviewSurface = egl.eglCreateWindowSurface(display, config, nativeWindow, null);

            Encoder.getEncoder().create();
            mRecordEGLSurface = egl.eglCreateWindowSurface(display, config, Encoder.getEncoder().getsurface(), null);

            gDisplay = display;
            gConfig  = config;

        } catch (IllegalArgumentException e) {
            Log.e(TAG, "eglCreateWindowSurface (native)", e);
        }
        // this return will triger Renderer
        result = mEGLPreviewSurface;
        return result;
    }

    public void destroySurface(EGL10 egl, EGLDisplay display, EGLSurface surface) {
        egl.eglDestroySurface(display, surface);
    }

    public void makeCurrentToPreview(EGLContext context){
            egl10.eglMakeCurrent(egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY),
                    mEGLPreviewSurface, mEGLPreviewSurface,context );
    }

    public void makeCurrentToRecord(EGLContext context){
        egl10.eglMakeCurrent(egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY),
                mRecordEGLSurface, mRecordEGLSurface,context );
    }

    public void swapRecordBuffers() {
            egl10.eglSwapBuffers(egl10.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY), mRecordEGLSurface);
    }
}