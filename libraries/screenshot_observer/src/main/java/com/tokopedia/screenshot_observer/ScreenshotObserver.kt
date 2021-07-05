package com.tokopedia.screenshot_observer

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore

class ScreenshotObserver(handler: Handler?, private val mContentResolver: ContentResolver, listener: Listener) : ContentObserver(handler) {

    private val MEDIA_EXTERNAL_URI_STRING = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()
    private val mListener = listener

    override fun onChange(selfChange: Boolean, uri: Uri) {
        super.onChange(selfChange, uri)
        if (isSingleImageFile(uri)) {
            mListener.onScreenShotTaken(uri)
        }
    }

    private fun isSingleImageFile(uri: Uri): Boolean {
        val pattern = "$MEDIA_EXTERNAL_URI_STRING/[0-9]+"
        return uri.toString().matches(pattern.toRegex())
    }

    interface Listener {
        fun onScreenShotTaken(uri: Uri)
    }
}
