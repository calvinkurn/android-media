package com.tkpd.remoteresourcerequest.task

import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.database.ResourceEntry
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.IllegalStateException


class ResourceDownloadRunnable(
        val task: TaskDownloadProperties,
        private val client: OkHttpClient,
        database: ResourceDB
) :
        Runnable {
    private lateinit var filePath: String
    private val resourceEntryDao = database.resourceEntryDao

    interface TaskDownloadProperties {
        fun handleDownloadState(state: Int)
        fun getFileLocationFromDirectory(): File
        fun getDownloadUrl(): String
        fun setByteBuffer(byteArray: ByteArray?)
    }

    @Throws(Exception::class)
    fun downloadFileSync() {
        val entry = readEntryFromDatabase()
        if (entry != null) {
            task.setByteBuffer(fileFromAbsolutePath(entry.absolutePath))
            task.handleDownloadState(DOWNLOAD_STATE_SKIPPED)
        } else {
            val request = Request.Builder()
                    .url(task.getDownloadUrl())
                    .build()
            try {
                task.handleDownloadState(DOWNLOAD_STATE_STARTED)
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException) {
                        task.handleDownloadState(DOWNLOAD_STATE_FAILED)
                    }

                    override fun onResponse(call: Call?, response: Response) {
                        if (!response.isSuccessful) {
                            task.handleDownloadState(DOWNLOAD_STATE_FAILED)
                            throw IOException("Failed to download file: $response")
                        }
                        val byteArray: ByteArray? = response.body()?.bytes()
                        byteArray?.let {
                            task.setByteBuffer(byteArray)
                            filePath = saveToCacheDirectory(byteArray)
                            writeEntryToDatabase()
                            task.handleDownloadState(DOWNLOAD_STATE_COMPLETED)
                        } ?: task.handleDownloadState(DOWNLOAD_STATE_FAILED)
                    }
                })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun fileFromAbsolutePath(absolutePath: String): ByteArray {
        return File(absolutePath).inputStream().readBytes()
    }

    private fun readEntryFromDatabase(): ResourceEntry? {
        val url = task.getDownloadUrl()
        return resourceEntryDao.getResourceEntry(task.getDownloadUrl())?.let {
            resourceEntryDao.updateLastAccess(System.currentTimeMillis(), url)
            it
        }
    }

    private fun writeEntryToDatabase() {
        resourceEntryDao.createResourceEntry(
                ResourceEntry(
                        task.getDownloadUrl(),
                        filePath,
                        "",
                        "",
                        System.currentTimeMillis(),
                        System.currentTimeMillis()
                )
        )
    }

    fun saveToCacheDirectory(byteArray: ByteArray): String {
        val file = task.getFileLocationFromDirectory()
        if (file.exists()) return file.absolutePath
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(file)
            out.write(byteArray)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            check(file.length() == byteArray.size.toLong()) { "Corrupt downloaded file!!" }
            out?.flush()
            out?.close()

        }
        return file.absolutePath
    }

    override fun run() {
        downloadFileSync()
    }

    companion object {
        const val DOWNLOAD_STATE_FAILED = 0
        const val DOWNLOAD_STATE_STARTED = 1
        const val DOWNLOAD_STATE_COMPLETED = 2
        const val DOWNLOAD_STATE_SKIPPED = 3

    }
}