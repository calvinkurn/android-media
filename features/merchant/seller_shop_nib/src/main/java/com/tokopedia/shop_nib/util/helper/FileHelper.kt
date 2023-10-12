package com.tokopedia.shop_nib.util.helper

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
            FirebaseCrashlytics.getInstance().recordException(e)
            0
        } finally {
            cursor?.close()
        }
    }

    fun delete(file: File) {
        file.delete()
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
        } catch (_: Exception) {
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (_: Exception) {
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
            FirebaseCrashlytics.getInstance().recordException(e)
            ""
        } finally {
            cursor?.close()
        }
    }

    fun getFileExtension(uri: Uri): String {
        val fileName = getFileName(uri)
        return fileName.substringAfterLast(".")
    }
}
