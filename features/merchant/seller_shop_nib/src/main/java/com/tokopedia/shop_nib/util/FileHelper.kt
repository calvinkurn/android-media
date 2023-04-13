package com.tokopedia.shop_nib.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject


class FileHelper @Inject constructor(@ApplicationContext private val context: Context) {

    fun getFileSizeInBytes(uri: Uri): Long {
        val cursor: Cursor? = context.contentResolver.query(
            uri, null, null, null, null, null
        )

        return try {
            if (cursor != null && cursor.moveToFirst()) {
                val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                cursor.getLong(fileSizeIndex)
            } else {
                0
            }
        } catch (e: Exception) {
            0
        } finally {
            cursor?.close()
        }
    }

    fun getFileFromUri(uri: Uri, filename: String): File? {
        var file: File? = null
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            file = File(context.cacheDir, filename)
            outputStream = FileOutputStream(file)
            inputStream?.let { input ->
                val buffer = ByteArray(4 * 1024) // 4K buffer
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return file
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
        } finally {
            cursor?.close()
        }
    }

    fun getFileExtension(uri: Uri): String {
        val fileName = getFileName(uri)
        return fileName.substringAfterLast(".")
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
}
