package com.tokopedia.utils.file

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.tokopedia.config.GlobalConfig
import java.io.*
import java.nio.channels.FileChannel
import java.util.*

object FileUtil {
    private const val SCHEME_CONTENT = "content"

    /**
     * get Tokopedia's internal directory
     * it will be create a new directory if doesn't exist
     */
    @JvmStatic
    fun getTokopediaInternalDirectory(relativePathFolder: String?, isCacheDir: Boolean = true): File {
        val isExternalWritable = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        var directory: File? = null
        if (isExternalWritable) {
            if (isCacheDir) {
                val rootCacheDirectory = File(GlobalConfig.EXTERNAL_CACHE_DIR)
                if (GlobalConfig.EXTERNAL_CACHE_DIR.isNotEmpty() && rootCacheDirectory.exists()) {
                    directory = File(rootCacheDirectory.absolutePath, relativePathFolder ?: "")
                }
            } else {
                val rootFileDirectory = File(GlobalConfig.EXTERNAL_FILE_DIR)
                if (GlobalConfig.EXTERNAL_FILE_DIR.isNotEmpty() && rootFileDirectory.exists()) {
                    directory = File(rootFileDirectory.absolutePath, relativePathFolder ?: "")
                }
            }
        }
        if (directory == null) {
            directory =
                    if (isCacheDir) {
                        File(GlobalConfig.INTERNAL_CACHE_DIR, relativePathFolder ?: "")
                    } else {
                        File(GlobalConfig.INTERNAL_FILE_DIR, relativePathFolder ?: "")
                    }
        }
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }

    @JvmStatic
    fun generateUniqueFileName(): String {
        var timeString = System.currentTimeMillis().toString()
        val length = timeString.length
        var startIndex = length - 5
        if (startIndex < 0) {
            startIndex = 0
        }
        timeString = timeString.substring(startIndex)
        return timeString + Random().nextInt(100)
    }

    fun writeStreamToFile(source: InputStream, file: File): Boolean {
        val outStream: OutputStream
        return try {
            outStream = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var length: Int
            //copy the file content in bytes
            while (source.read(buffer).also { length = it } > 0) {
                outStream.write(buffer, 0, length)
            }
            source.close()
            outStream.close()
            true
        } catch (e: Throwable) {
            false
        }
    }

    fun writeBufferToFile(buffer: ByteArray, path: String): Boolean {
        var fos: FileOutputStream? = null
        return try {
            fos = FileOutputStream(path)
            fos.write(buffer)
            true
        } catch (e: Throwable) {
            System.gc()
            false
        } finally {
            try {
                fos?.close()
            } catch (ignored: Exception) { }
        }
    }

    @JvmStatic
    fun deleteFile(path: String?) {
        if (path?.isNotEmpty() == true) {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
    }

    @Throws(IOException::class)
    fun copyFile(pathFrom: String, pathTo: String) {
        if (pathFrom.equals(pathTo, ignoreCase = true)) {
            return
        }
        var outputChannel: FileChannel? = null
        var inputChannel: FileChannel? = null
        try {
            inputChannel = FileInputStream(File(pathFrom)).channel
            outputChannel = FileOutputStream(File(pathTo)).channel
            inputChannel.transferTo(0, inputChannel.size(), outputChannel)
            inputChannel.close()
        } finally {
            inputChannel?.close()
            outputChannel?.close()
        }
    }

    @JvmStatic
    fun getFileSizeInKb(filePath: String): Long {
        return getFileSizeInKb(File(filePath))
    }

    fun getFileSizeInKb(file: File): Long {
        return file.length() / 1024
    }

    private fun isImageMimeType(context: Context, uri: Uri): Boolean {
        val mimeType: String? = getMimeType(context, uri)
        return mimeType?.isNotEmpty() == true && mimeType.startsWith("image")
    }

    private fun isImageMimeType(mimeType: String): Boolean {
        return mimeType.isNotEmpty() && mimeType.startsWith("image")
    }

    @JvmStatic
    fun isImageType(context: Context, filePath: String?): Boolean {
        return isImageMimeType(context, Uri.fromFile(File(filePath)))
    }

    private fun getMimeType(context: Context, uri: Uri): String? {
        val mimeType: String?
        mimeType = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cr = context.contentResolver
            cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase(Locale.getDefault()))
        }
        return mimeType
    }

    @JvmStatic
    fun getPath(resolver: ContentResolver, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        if (SCHEME_CONTENT == uri.scheme) {
            var cursor: Cursor? = null
            return try {
                cursor = resolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA),
                        null, null, null)
                if (cursor == null || !cursor.moveToFirst()) {
                    null
                } else cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA))
            } finally {
                cursor?.close()
            }
        }
        return uri.path
    }

}