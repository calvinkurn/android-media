package com.tokopedia.play.broadcaster.util.asset.manager

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.util.asset.FileUtil
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 09, 2023
 */
class AssetManagerImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
) : AssetManager {

    override suspend fun unzipAndSave(
        responseBody: ResponseBody,
        fileName: String,
        filePath: String,
        folderPath: String
    ): Boolean {
        return try {
            withContext(dispatchers.io) {
                val fileNameWithExtension = fileName + ZIP_EXTENSION
                val completePath = filePath + File.separator + fileNameWithExtension

                val file = File(filePath)
                if (!file.exists()) {
                    if(FileUtil.writeResponseBodyToDisk(filePath, fileNameWithExtension, responseBody)) {
                        val isUnzipSuccess = FileUtil.unzipFile(completePath, folderPath)
                        if (isUnzipSuccess) {
                            FileUtil.deleteFile(completePath)
                        }
                        isUnzipSuccess
                    }
                    else false
                }
                else true
            }
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        private const val ZIP_EXTENSION = ".zip"
    }
}
