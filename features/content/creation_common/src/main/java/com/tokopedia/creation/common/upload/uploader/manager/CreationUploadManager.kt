package com.tokopedia.creation.common.upload.uploader.manager

import androidx.work.ForegroundInfo
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadStatus
import com.tokopedia.creation.common.upload.model.CreationUploadSuccessData
import com.tokopedia.creation.common.upload.uploader.notification.CreationUploadNotificationManager

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
abstract class CreationUploadManager(
    private val notificationManager: CreationUploadNotificationManager?
) {

    private var mCurrentProgress = 0

    private var mListener: CreationUploadManagerListener? = null

    fun setupManager(listener: CreationUploadManagerListener) {
        mCurrentProgress = 0
        mListener = listener
    }

    abstract suspend fun execute(
        notificationId: Int
    ): CreationUploadExecutionResult

    protected fun updateProgress(
        uploadData: CreationUploadData,
        progress: Int,
    ) {
        mCurrentProgress = progress
        broadcastProgress(uploadData, CreationUploadStatus.Upload, mCurrentProgress)
        notificationManager?.onProgress(mCurrentProgress)
    }

    protected suspend fun broadcastInit(
        uploadData: CreationUploadData,
        notificationId: Int,
    ) {
        broadcastProgress(uploadData, CreationUploadStatus.Upload)
        notificationManager?.init(uploadData, notificationId)

        notificationManager?.let {
            mListener?.setupForegroundNotification(notificationManager.onStart())
        }
    }

    protected fun broadcastComplete(
        uploadData: CreationUploadData,
        successData: CreationUploadSuccessData = CreationUploadSuccessData.Empty,
    ) {
        broadcastProgress(uploadData, CreationUploadStatus.Success)
        notificationManager?.onSuccess(successData)
    }

    protected fun broadcastFail(uploadData: CreationUploadData) {
        broadcastProgress(uploadData, CreationUploadStatus.Failed)
        notificationManager?.onError()
    }

    protected fun broadcastProgress(
        uploadData: CreationUploadData,
        uploadStatus: CreationUploadStatus,
        progress: Int = mCurrentProgress,
    ) {
        mListener?.setProgress(uploadData, progress, uploadStatus)
    }

    protected fun broadcastUpdateData(
        uploadData: CreationUploadData,
    ) {
        mListener?.updateData(uploadData)
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

    fun updateData(
        uploadData: CreationUploadData,
    )
}

sealed interface CreationUploadExecutionResult {
    object Success : CreationUploadExecutionResult

    data class Error(
        val uploadData: CreationUploadData,
        val throwable: Throwable,
    ) : CreationUploadExecutionResult
}
