package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.common.logger.trackToTimber
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.addPrefix
import java.io.File

interface UploaderManager {

    fun setProgressUploader(progress: ProgressUploader?)

    fun setError(message: String, sourceId: String, file: File): UploadResult {
        if (message.isNotEmpty()) {
            trackToTimber(file, sourceId, message)
        }
        return UploadResult.Error(message.addPrefix())
    }

}