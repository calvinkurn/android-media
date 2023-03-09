package com.tokopedia.play.broadcaster.util.asset

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
class AssetDownloaderImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val downloadManager: AssetDownloadManager,
) : AssetDownloader {

    override suspend fun downloadUnzip(
        url: String,
        fileName: String,
        filePath: String,
        folderPath: String,
    ): Boolean {
        return try {
            val completePath = filePath + File.separator + fileName
            val file = File(completePath)
            if (!file.exists()) {
                val responseBody = withContext(dispatchers.io) {
                    downloadManager.download(url)
                }

                if(FileUtil.writeResponseBodyToDisk(filePath, fileName, responseBody)) {
                    val isUnzipSuccess = FileUtil.unzipFile(completePath, folderPath)
                    if (isUnzipSuccess) {
                        FileUtil.deleteFile(completePath)
                    }
                    isUnzipSuccess
                }
                else false
            }
            else true
        } catch (e: Exception) {
            false
        }
    }
}
