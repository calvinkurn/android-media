package com.tokopedia.creation.common.upload.uploader.manager

import androidx.work.ForegroundInfo
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadStatus
import com.tokopedia.creation.common.upload.uploader.notification.CreationUploadNotificationManager

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
abstract class CreationUploadManager(
    private val notificationManager: CreationUploadNotificationManager?
) {

    private var currentProgress = 0

    protected var mListener: CreationUploadManagerListener? = null

    fun setListener(listener: CreationUploadManagerListener) {
        mListener = listener
    }

    abstract suspend fun execute(uploadData: CreationUploadData): Boolean

    protected fun updateProgress(
        uploadData: CreationUploadData,
        progress: Int,
    ) {
        currentProgress = progress
        broadcastProgress(uploadData, CreationUploadStatus.Upload, currentProgress)
        notificationManager?.onProgress(currentProgress)
    }

    protected suspend fun broadcastInit(uploadData: CreationUploadData) {
        broadcastProgress(uploadData, CreationUploadStatus.Upload)
        notificationManager?.init(uploadData)

        notificationManager?.let {
            mListener?.setupForegroundNotification(notificationManager.onStart())
        }
    }

    protected fun broadcastComplete(uploadData: CreationUploadData) {
        broadcastProgress(uploadData, CreationUploadStatus.Success)
        notificationManager?.onSuccess()
    }

    protected fun broadcastFail(uploadData: CreationUploadData) {
        broadcastProgress(uploadData, CreationUploadStatus.Failed)
        notificationManager?.onError()
    }

    protected fun broadcastProgress(
        uploadData: CreationUploadData,
        uploadStatus: CreationUploadStatus,
        progress: Int = currentProgress,
    ) {
        mListener?.setProgress(uploadData, progress, uploadStatus)
    }

    companion object {
        const val MAX_UPLOAD_PROGRESS = 100
    }
}

interface CreationUploadManagerListener {

    fun setupForegroundNotification(info: ForegroundInfo)

    fun setProgress(
        uploadData: CreationUploadData,
        progress: Int,
        uploadStatus: CreationUploadStatus,
    )
}
