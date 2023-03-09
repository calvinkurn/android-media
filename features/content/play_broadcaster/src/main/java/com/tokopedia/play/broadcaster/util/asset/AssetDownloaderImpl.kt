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

    override suspend fun downloadUnzip(fileUrl: String, fileName: String, filePath: String): Boolean {
        return try {
            val file = File(filePath + File.separator + fileName)
            if (!file.exists()) {
                val responseBody = withContext(dispatchers.io) {
                    downloadManager.download(fileUrl)
                }

                if(FileUtil.writeResponseBodyToDisk(filePath, fileName, responseBody)) {
                    val isUnzipSuccess = FileUtil.unzipFile(filePath + File.separator + fileName, filePath)
                    if (isUnzipSuccess) {
                        FileUtil.deleteFile(filePath + File.separator + fileName)
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
