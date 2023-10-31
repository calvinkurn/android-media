package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.analytics.UploaderLogger
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.addPrefix
import com.tokopedia.mediauploader.common.util.fileExtension
import java.io.File

interface UploaderManager {

    fun setProgressUploader(progress: ProgressUploader?)

    fun allowedExt(filePath: String, allowedExtensions: String): Boolean {
        val fileExt = filePath
            .fileExtension()
            .lowercase()

        val allowed = allowedExtensions
            .split(",")
            .map { it.drop(NUM_EXT_DROP_FIRST) }

        return allowed.contains(fileExt)
    }

    fun setError(message: String, sourceId: String, file: File): UploadResult {
        if (message.isNotEmpty()) UploaderLogger.commonWithoutReqIdError(sourceId, message)
        return UploadResult.Error(message.addPrefix())
    }

    companion object {
        private const val NUM_EXT_DROP_FIRST = 1
    }

}
