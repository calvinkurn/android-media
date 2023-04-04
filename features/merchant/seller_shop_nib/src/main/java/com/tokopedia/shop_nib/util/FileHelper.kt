package com.tokopedia.shop_nib.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class FileHelper @Inject constructor(@ApplicationContext private val context: Context) {

    fun getFileFromUri(uri: Uri): File? {
        val filePath = getRealPathFromUri(context, uri) ?: return null
        return File(filePath)
    }

    fun getFileSizeInBytes(uri: Uri): Long {
        val contentResolver = context.contentResolver
        var inputStream: InputStream? = null
        var fileSize: Long = 0

        try {
            inputStream = contentResolver.openInputStream(uri)
            fileSize = inputStream?.available()?.toLong() ?: 0
        } catch (_: IOException) {
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return fileSize
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
