package com.tokopedia.shop_nib.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.OpenableColumns
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class FileHelper @Inject constructor(@ApplicationContext private val context: Context) {

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

    fun getFileName(uri: Uri): String {
        val cursor: Cursor? = context.contentResolver.query(
            uri, null, null, null, null, null
        )

        return try {
            if (cursor != null && cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.getString(displayNameIndex)
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    fun generatePdfThumbnail(uri: Uri): Bitmap? {
        val contentResolver = context.contentResolver
        var renderer: PdfRenderer? = null
        var bitmap: Bitmap? = null
        try {
            val fileDescriptor = contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
            val parcelFileDescriptor = ParcelFileDescriptor.dup(fileDescriptor)
            renderer = PdfRenderer(parcelFileDescriptor)
            if (renderer.pageCount > 0) {
                val page = renderer.openPage(0)
                val width = 80
                val height = 80
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val matrix = Matrix()
                val scale = width.toFloat() / page.width
                matrix.setScale(scale, scale)
                page.render(bitmap, null, matrix, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                page.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            renderer?.close()
        }
        return bitmap
    }

    fun getFileExtension(uri: Uri): String {
        val fileName = getFileName(uri)
        return fileName.substringAfterLast(".")
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
