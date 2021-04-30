package com.tokopedia.utils.file

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.*

/***
 * Util to put file/image/video into public folder without using legacy Environment.getExternalStoragePublicDirectory
 * No need to request WRITE_EXTERNAL_STORAGE.
 */
object PublicFolderUtil {

    fun putImageToPublicFolder(context: Context, bitmap: Bitmap, fileName: String,
                               compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
                               mimeType: String = "image/jpeg",
                               directory: String = Environment.DIRECTORY_PICTURES): String? {
        try {
            val contentResolver: ContentResolver = context.contentResolver

            val contentValues = createContentValues(fileName, mimeType, directory)

            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                contentResolver.openOutputStream(uri)?.let { outputStream ->
                    BufferedOutputStream(outputStream)
                    bitmap.compress(compressFormat, 100, outputStream)
                    outputStream.close()
                }
                contentValues.clear()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(uri, contentValues, null, null)
                }
            }
            return FileUtil.getPath(contentResolver, uri)
        } catch (ex: Exception) {
            return null
        }
    }

    fun putFileToPublicFolder(context: Context,
                              localFile: File,
                              outputFileName:String,
                              mimeType: String,
                              directory: String = Environment.DIRECTORY_DOWNLOADS): String? {
        try {
            val contentResolver: ContentResolver = context.contentResolver

            val contentValues = createContentValues(outputFileName, mimeType, directory)

            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                contentResolver.openOutputStream(uri)?.let { outputStream ->
                    var inputStream: FileInputStream? = null
                    try {
                        inputStream = FileInputStream(localFile)
                        copy(inputStream, outputStream)
                    } finally {
                        inputStream?.close()
                        outputStream.close()
                    }
                }
                contentValues.clear()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(uri, contentValues, null, null)
                }
            }
            return FileUtil.getPath(contentResolver, uri)
        } catch (ex: Exception) {
            return null
        }
    }

    private fun createContentValues(fileName: String, mimeType: String, directory: String): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    directory
            )
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        return contentValues
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }
}