package com.tokopedia.sellerfeedback.presentation.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.tokopedia.screenshot_observer.ScreenshotData
import com.tokopedia.sellerfeedback.presentation.uimodel.ImageFeedbackUiModel

class ScreenshotManager(private val context: Context) {

    companion object {
        private val PROJECTION = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        )
    }

    fun getUiModel(uri: Uri): ImageFeedbackUiModel? {
        val screenshotData = handleItem(uri)
        return if (screenshotData != null) {
            ImageFeedbackUiModel(
                    imageUrl = screenshotData.path
            )
        } else null
    }

    private fun handleItem(uri: Uri): ScreenshotData? {
        val contentResolver: ContentResolver = context.contentResolver
        var result: ScreenshotData? = null
        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(uri, PROJECTION, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val screenshotData = generateScreenshotDataFromCursor(cursor)
                result = screenshotData
            }
        } finally {
            cursor?.close()
        }
        return result
    }

    private fun generateScreenshotDataFromCursor(cursor: Cursor): ScreenshotData {
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
        val fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        return ScreenshotData(id, fileName, path)
    }

}