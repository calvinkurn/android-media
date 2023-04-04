package com.tokopedia.shop_nib.presentation.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import javax.inject.Inject

class FileHelper @Inject constructor() {

    fun getFileFromUri(context: Context, uri: Uri): File? {
        val filePath = getRealPathFromUri(context, uri)
        return if (filePath != null) File(filePath) else null
    }

    private fun getRealPathFromUri(context: Context, uri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver?.query(uri, projection, null, null, null)
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                path = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        if (path == null) {
            // fallback to using the uri directly
            path = uri.path
        }
        return path
    }
}
