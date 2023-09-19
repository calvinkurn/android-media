package com.tokopedia.creation.common.upload.uploader.manager

import androidx.work.ForegroundInfo
import com.tokopedia.creation.common.upload.model.CreationUploadQueue
import com.tokopedia.creation.common.upload.model.CreationUploadResult

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
interface CreationUploadManager {

    suspend fun execute(
        uploadData: CreationUploadQueue,
        listener: CreationUploadManagerListener,
    ): CreationUploadResult
}

interface CreationUploadManagerListener {

    suspend fun setupForegroundNotification(info: ForegroundInfo)

    suspend fun setProgress(uploadData: CreationUploadQueue, progress: Int)
}
