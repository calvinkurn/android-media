package com.tokopedia.creation.common.upload.uploader.manager

import androidx.work.ForegroundInfo
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadResult
import com.tokopedia.creation.common.upload.model.CreationUploadStatus

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
interface CreationUploadManager {

    suspend fun execute(
        uploadData: CreationUploadData,
        listener: CreationUploadManagerListener,
    ): Boolean

    companion object {
        const val UPLOAD_FINISH_DELAY = 1000L
        const val MAX_UPLOAD_PROGRESS = 100
    }
}

interface CreationUploadManagerListener {

    suspend fun setupForegroundNotification(info: ForegroundInfo)

    fun setProgress(
        uploadData: CreationUploadData,
        progress: Int,
        uploadStatus: CreationUploadStatus,
    )
}
