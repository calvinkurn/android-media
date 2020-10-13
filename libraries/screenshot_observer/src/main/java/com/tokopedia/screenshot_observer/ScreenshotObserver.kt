package com.tokopedia.screenshot_observer

import android.content.ContentResolver
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore

class ScreenshotObserver(handler: Handler?, private val mContentResolver: ContentResolver, listener: Listener) : ContentObserver(handler) {

    private val MEDIA_EXTERNAL_URI_STRING = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()
    private val mListener = listener

    override fun onChange(selfChange: Boolean, uri: Uri) {
        super.onChange(selfChange, uri)
        if (isSingleImageFile(uri)) {
            handleItem(uri)
        }
    }

    private fun isFileScreenshot(fileName: String): Boolean {
        return fileName.toLowerCase().startsWith(FILE_NAME_PREFIX)
    }

    private fun isSingleImageFile(uri: Uri): Boolean {
        val pattern = "$MEDIA_EXTERNAL_URI_STRING/[0-9]+"
        return uri.toString().matches(pattern.toRegex())
    }

    private fun handleItem(uri: Uri) {
        var cursor: Cursor? = null
        try {
            cursor = mContentResolver.query(uri, PROJECTION, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val Name = generateScreenshotDataFromCursor(cursor)
                if (Name != null) {
                    Handler(Looper.getMainLooper()).post {
                        mListener.onScreenShotTaken(uri) }
                }
            }
        } finally {
            cursor?.close()
        }
    }

    private fun generateScreenshotDataFromCursor(cursor: Cursor): String? {
        val fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
        return if (isFileScreenshot(fileName)) {
            fileName
        } else {
            null
        }
    }

    interface Listener {
        fun onScreenShotTaken(uri: Uri)
    }

    companion object {
        private val FILE_NAME_PREFIX = "screenshot"
        private val PROJECTION = arrayOf(
                MediaStore.Images.Media.DISPLAY_NAME
        )
    }

}
