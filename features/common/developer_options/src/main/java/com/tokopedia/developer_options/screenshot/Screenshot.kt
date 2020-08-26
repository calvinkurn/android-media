package com.tokopedia.developer_options.screenshot

import android.content.ContentResolver
import android.database.ContentObserver
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore


class Screenshot(contentResolver: ContentResolver, listener: Listener) {
    private val mHandlerThread: HandlerThread
    private val mHandler: Handler
    private val mContentResolver: ContentResolver
    private val mContentObserver: ContentObserver
    private val mListener: Listener
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

    init {
        mHandlerThread = HandlerThread("ShotWatch")
        mHandlerThread.start()
        mHandler = Handler(mHandlerThread.looper)
        mContentResolver = contentResolver
        mContentObserver = ScreenShotObserver(mHandler, contentResolver, listener)
        mListener = listener
    }
}