package com.tokopedia.effect.util.asset.manager

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.effect.util.FileUtil
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
                val fileNameWithExtension = if(fileName.endsWith(ZIP_EXTENSION)) fileName else fileName + ZIP_EXTENSION
                val completePath = filePath + File.separator + fileNameWithExtension

                val file = File(filePath)
                if (!file.exists()) {
                    if(FileUtil.writeResponseBodyToDisk(
                            filePath,
                            fileNameWithExtension,
                            responseBody
                        )
                    ) {
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

    override suspend fun save(
        responseBody: ResponseBody,
        fileName: String,
        folderPath: String,
    ): Boolean {
        return try {
            withContext(dispatchers.io) {
                val isFileExists = File(folderPath).listFiles()?.any { it.name == fileName } ?: false
                if (!isFileExists) {
                    FileUtil.writeResponseBodyToDisk(folderPath, fileName, responseBody)
                }
                else {
                    true
                }
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteDirectory(directory: String) {
        val file = File(directory)

        if(file.isDirectory) {
            file.listFiles()?.forEach {
                deleteDirectory(it.absolutePath)
            }
        }
        file.delete()
    }

    companion object {
        private const val ZIP_EXTENSION = ".zip"
    }
}
