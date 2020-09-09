package com.tokopedia.screenshot_observer

import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import com.tokopedia.config.GlobalConfig


open class Screenshot(contentResolver: ContentResolver, listener: Listener) : Application.ActivityLifecycleCallbacks {
    private val mHandlerThread: HandlerThread = HandlerThread("ScreenshotObserver")
    private val mHandler: Handler
    private val mContentResolver: ContentResolver
    private val mContentObserver: ContentObserver
    private val mListener: Listener

    init {
        mHandlerThread.start()
        mHandler = Handler(mHandlerThread.looper)
        mContentResolver = contentResolver
        mContentObserver = ScreenshotObserver(mHandler, contentResolver, listener)
        mListener = listener
    }

    fun register() {
        mContentResolver.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                mContentObserver
        )
    }

    fun unregister() {
        mContentResolver.unregisterContentObserver(mContentObserver)
    }

    interface Listener {
        fun onScreenShotTaken(screenshotData: ScreenshotData?)
    }

    override fun onActivityPaused(activity: Activity) {
        if (!GlobalConfig.isSellerApp()) {
            unregister()
        }
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
        if (!GlobalConfig.isSellerApp()) {
            register()
        }
    }


}