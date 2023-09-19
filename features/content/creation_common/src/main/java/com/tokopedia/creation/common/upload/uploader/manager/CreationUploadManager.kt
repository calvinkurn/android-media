package com.tokopedia.creation.common.upload.uploader.manager

import androidx.work.ForegroundInfo
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadResult

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
interface CreationUploadManager {

    suspend fun execute(
        uploadData: CreationUploadData,
        listener: CreationUploadManagerListener,
    ): CreationUploadResult
}

interface CreationUploadManagerListener {

    suspend fun setupForegroundNotification(info: ForegroundInfo)

    suspend fun setProgress(uploadData: CreationUploadData, progress: Int)
}
