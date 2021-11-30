package com.tkpd.remoteresourcerequest.runnable

import com.tkpd.remoteresourcerequest.database.ResourceDB
import com.tkpd.remoteresourcerequest.database.ResourceEntry
import com.tkpd.remoteresourcerequest.utils.Constants.CORRUPT_FILE_MESSAGE
import com.tkpd.remoteresourcerequest.utils.Constants.URL_SEPARATOR
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Semaphore


class ResourceDownloadRunnable(
    val task: TaskDownloadProperties,
    private val client: OkHttpClient,
    database: ResourceDB
) :
    Runnable {
    private lateinit var filePath: String
    private val resourceEntryDao = database.resourceEntryDao
    private val url = task.getDownloadUrl()
    private val fileName = url.substring(url.lastIndexOf(URL_SEPARATOR) + 1)
    private val downloadFailMessage = "Failed to download file: %s"

    interface TaskDownloadProperties {
        fun resourceVersion(): String
        fun handleDownloadState(state: Int)
        fun getFileLocationFromDirectory(): File
        fun getDownloadUrl(): String
        fun setByteBuffer(byteArray: ByteArray?)
    }

    @Throws(Exception::class)
    fun downloadFileSync() {
        var hasSomeExceptionOccurred = false
        /**
         * At this point all threads will enter simultaneously out of order so let them
         * create unique [Semaphore] objects sequentially in [syncMap] for each [url]. Each
         * Semaphore will permit only 1 Thread to acquire it.
         *
         */
        synchronized(syncMap) {
            syncMap[url] ?: kotlin.run {
                syncMap[url] = Semaphore(1)
            }
        }

        syncMap[url]?.let { semaphore ->
            /**
             * Now that the [syncMap] has a unique Semaphore for each [url]s, we need to
             * synchronize the subsequent flow between two Threads requesting for the same [url].
             *
             * All the Threads which are requesting for different [url] will acquire lock on
             * different [semaphore]s, thus they execute concurrently.
             *
             * If 2 Threads request for same [url] this synchronized block will let only 1 of
             * them to execute.
             */
            synchronized(semaphore) {

                /**
                 * Since [semaphore] permits only 1 thread so other one will wait here until it is
                 * released.
                 *
                 * Why we need Semaphore?
                 * Because okhttp enqueues any download request in its thread. So
                 * synchronized block above may finish first even when download(being in
                 * another thread) is still pending.
                 * Using Semaphore here ensures that Producer Thread (whichever starts the download
                 * first) should complete and till then Consumer Threads (those waiting to download
                 * from the same url) should wait.
                 *
                 * Example: Suppose 2 threads, T1 and T2, are requesting to download same resource
                 * (i.e. same [url])
                 *
                 * T1 acquires this [semaphore] and requested okhttp to download from [url]. After
                 * that it exits this synchronized block. Suppose download is still pending due to
                 * low network. Meanwhile, T2 gets the chance to enter this synchronized block. But
                 * it will find that this [semaphore] is still acquired by T1. So it will go in
                 * waiting state. Now T1 can end up in any of these states:
                 *  1. download success
                 *  2. download fails / okhttp timeout occurs.
                 *  3. some unwanted exception occurred.
                 *
                 * In any of the above scenario T1 must ensure to release the acquired [semaphore].
                 * For point 1 and 2 we will get onResponse/onFailure. So we released it there.
                 * However for point 3, we have released the [semaphore] in finally block.
                 *
                 */
                semaphore.acquire()

                task.handleDownloadState(DOWNLOAD_STATE_STARTED)
                val entry = readEntryFromDatabase()
                if (entry != null) {
                    task.setByteBuffer(entry)
                    task.handleDownloadState(DOWNLOAD_STATE_SKIPPED)
                    semaphore.release()
                } else {
                    val request = Request.Builder().url(url).build()
                    try {
                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call?, e: IOException) {
                                task.handleDownloadState(DOWNLOAD_STATE_FAILED)
                                semaphore.release()
                            }

                            override fun onResponse(call: Call?, response: Response) {
                                if (!response.isSuccessful) {
                                    task.handleDownloadState(DOWNLOAD_STATE_FAILED)
                                    semaphore.release()
                                    throw IOException(downloadFailMessage.format(response))
                                }
                                val byteArray: ByteArray? = response.body()?.bytes()
                                byteArray?.let {
                                    task.setByteBuffer(byteArray)
                                    filePath = saveToCacheDirectory(byteArray)
                                    writeEntryToDatabase()
                                    task.handleDownloadState(DOWNLOAD_STATE_COMPLETED)
                                } ?: task.handleDownloadState(DOWNLOAD_STATE_FAILED)
                                semaphore.release()
                            }
                        })
                    } catch (e: Exception) {
                        e.printStackTrace()
                        hasSomeExceptionOccurred = true
                    } finally {
                        if (hasSomeExceptionOccurred) {
                            task.handleDownloadState(DOWNLOAD_STATE_FAILED)
                            semaphore.release()
                        }
                    }
                }
            }
        } ?: run {
            // Safe-Check: Can this be ever null!
            task.handleDownloadState(DOWNLOAD_STATE_FAILED)

        }
    }

    fun fileFromAbsolutePath(absolutePath: String): ByteArray? {
        return try {
            File(absolutePath).inputStream().readBytes()
        } catch (e: Exception) {
            null
        }
    }

    private fun readEntryFromDatabase(): ByteArray? {

        val entry = resourceEntryDao.getResourceEntry(fileName)
        return entry?.let {
            val byteArray = fileFromAbsolutePath(it.absolutePath)
            if(byteArray == null) {
                resourceEntryDao.deleteEntry(fileName)
                return null
            }
            resourceEntryDao.updateLastAccess(System.currentTimeMillis(), fileName)
            byteArray
        }
    }

    private fun writeEntryToDatabase() {
        resourceEntryDao.createResourceEntry(
            ResourceEntry(
                fileName,
                filePath,
                "",
                task.resourceVersion(),
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
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            out?.flush()
            out?.close()
            check(file.length() == byteArray.size.toLong()) { CORRUPT_FILE_MESSAGE }
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
        private val syncMap = HashMap<String, Semaphore>()
    }
}
