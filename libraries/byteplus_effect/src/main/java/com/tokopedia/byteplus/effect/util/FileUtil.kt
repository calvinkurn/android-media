package com.tokopedia.byteplus.effect.util

import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import okhttp3.ResponseBody
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
object FileUtil {

    private const val BUFFER_SIZE = 4096

    fun unzipFile(filePath: String, dirPath: String): Boolean {
        val dstDir = File(dirPath)
        if (!dstDir.exists() && !dstDir.mkdirs()) {
            EffectLogger.sendErrorCreateDir(dstDir.absolutePath)
            throw IllegalStateException("mkdir failed")
        }
        try {
            val zipInputStream = ZipInputStream(FileInputStream(File(filePath)))
            return unzipFile(zipInputStream, dstDir)
        } catch (e: Exception) {
            EffectLogger.sendErrorUnzip(e, dstDir.absolutePath)
        }
        return false
    }

    fun deleteFile(filePath: String): Boolean {
        val soonToBeDeleted = File(filePath)
        return if (soonToBeDeleted.exists()) {
            soonToBeDeleted.delete()
        } else false
    }

    fun writeResponseBodyToDisk(dirPath: String, fileName: String, body: ResponseBody): Boolean {
        return try {
            val dir = File(dirPath)
            val futureFile = File(dirPath, fileName)

            if (!dir.exists() && !dir.mkdirs()) {
                EffectLogger.sendErrorCreateDir(dir.absolutePath)
                throw IllegalStateException("mkdir failed")
            }
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(BUFFER_SIZE)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureFile)
                while (true) {
                    val read = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                EffectLogger.sendErrorWriteToDisk(e, dirPath, fileName)
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            EffectLogger.sendErrorWriteToDisk(e, dirPath, fileName)
            false
        }
    }

    private fun unzipFile(zipInputStream: ZipInputStream, dstDir: File): Boolean {
        try {
            if (dstDir.exists()) dstDir.delete()
            dstDir.mkdirs()
            var entry: ZipEntry?
            var name: String
            do {
                entry = zipInputStream.nextEntry
                if (null != entry) {
                    name = entry.name
                    if (entry.isDirectory) {
                        name = name.substring(0, name.length - 1)
                        val folder = File(dstDir, name)
                        folder.mkdirs()
                    } else {
                        val file = File(dstDir, name)
                        file.parentFile.mkdirs()
                        file.createNewFile()
                        val out = FileOutputStream(file)
                        var len: Int
                        val buffer = ByteArray(1024)
                        while (zipInputStream.read(buffer).also { len = it } != -1) {
                            out.write(buffer, 0, len)
                            out.flush()
                        }
                        out.close()
                    }
                }
            } while (null != entry)
        } catch (e: Exception) {
            EffectLogger.sendErrorUnzip(e, dstDir.absolutePath)
            return false
        } finally {
            zipInputStream.close()
        }
        return true
    }
}

