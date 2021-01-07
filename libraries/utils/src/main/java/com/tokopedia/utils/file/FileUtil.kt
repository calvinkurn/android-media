package com.tokopedia.utils.file

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import com.tokopedia.config.GlobalConfig
import java.io.*
import java.nio.channels.FileChannel
import java.util.*

object FileUtil {
    /**
     * get Tokopedia's internal directory
     * it will be create a new directory if doesn't exist
     */
    @JvmStatic
    fun getTokopediaInternalDirectory(relativePathFolder: String?, isCacheDir: Boolean = true): File {
        val isExternal = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        val directory: File
        if (isExternal) {
            if (isCacheDir) {
                val rootCacheDirectory = File(GlobalConfig.EXTERNAL_CACHE_DIR)
                if (rootCacheDirectory.exists()) {
                    directory = File(rootCacheDirectory.absolutePath, relativePathFolder ?: "")
                } else {
                    directory = File(GlobalConfig.INTERNAL_CACHE_DIR, relativePathFolder ?: "")
                }
            } else {
                val rootFileDirectory: File = File(GlobalConfig.EXTERNAL_FILE_DIR)
                if (rootFileDirectory.exists()) {
                    directory = File(rootFileDirectory.absolutePath, relativePathFolder ?: "")
                } else {
                    directory = File(GlobalConfig.INTERNAL_FILE_DIR, relativePathFolder ?: "")
                }
            }
        } else {
            if (isCacheDir) {
                directory = File(GlobalConfig.INTERNAL_CACHE_DIR, relativePathFolder ?: "")
            } else {
                directory = File(GlobalConfig.INTERNAL_FILE_DIR, relativePathFolder ?: "")
            }
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
        return try {
            val fos = FileOutputStream(path)
            fos.write(buffer)
            fos.close()
            true
        } catch (e: Throwable) {
            false
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
        var mimeType: String?
        mimeType = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cr = context.contentResolver
            cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase())
        }
        return mimeType
    }

}