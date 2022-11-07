package com.tokopedia.tokochat.util

import com.tokopedia.config.GlobalConfig
import com.tokopedia.utils.file.FileUtil
import java.io.File
import java.io.InputStream

object TokoChatViewUtil {

    private const val TOKOCHAT_RELATIVE_PATH = "/TokoChat"
    private const val JPEG_EXT = ".jpeg"

    fun downloadAndSaveByteArrayImage(
        fileName: String,
        inputStream: InputStream,
        onImageReady: (File?) -> Unit,
        onError: () -> Unit
    ) {
        try {
            val imageResult: File? = writeImageToTokoChatPath(inputStream.readBytes(), fileName)
            onImageReady(imageResult)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            onError()
        } finally {
            closeInputStream(inputStream)
        }
    }

    private fun closeInputStream(inputStream: InputStream) {
        try {
            inputStream.close()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    private fun writeImageToTokoChatPath(buffer: ByteArray?, fileName: String): File? {
        if (buffer != null) {
            val photo: File = getTokoChatPhotoPath(fileName)
            if (photo.exists()) {
                photo.delete()
            }
            if (FileUtil.writeBufferToFile(buffer, photo.path)) {
                return photo
            }
        }
        return null
    }

    fun getTokoChatPhotoPath(fileName: String): File {
        return File(getInternalCacheDirectory().absolutePath, fileName + JPEG_EXT)
    }

    private fun getInternalCacheDirectory(): File {
        val directory = File(GlobalConfig.INTERNAL_CACHE_DIR, TOKOCHAT_RELATIVE_PATH)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }
}
