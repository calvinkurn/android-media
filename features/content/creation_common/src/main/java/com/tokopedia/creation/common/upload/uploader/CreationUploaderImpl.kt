package com.tokopedia.creation.common.upload.uploader

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.creation.common.upload.const.CreationUploadConst
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.domain.usecase.post.DeleteMediaPostCacheUseCase
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadResult
import com.tokopedia.creation.common.upload.model.CreationUploadStatus
import com.tokopedia.creation.common.upload.uploader.worker.CreationUploaderWorker
import com.tokopedia.creation.common.upload.util.logger.CreationUploadLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class CreationUploaderImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val workManager: WorkManager,
    private val creationUploadQueueRepository: CreationUploadQueueRepository,
    private val deleteMediaPostCacheUseCase: DeleteMediaPostCacheUseCase,
    private val logger: CreationUploadLogger,
    private val gson: Gson,
) : CreationUploader {

    private val uploadResultFlow = MutableSharedFlow<CreationUploadResult>()

    override suspend fun upload(data: CreationUploadData) {
        if (isTopQueueFailed()) {
            creationUploadQueueRepository.clearQueue()
        }

        creationUploadQueueRepository.insert(data)
        startWorkManager()
    }

    override fun observe(): Flow<CreationUploadResult> {
        return creationUploadQueueRepository
            .observeTopQueue()
            .distinctUntilChanged()
            .mapNotNull { data ->
                try {
                    if (data == null) return@mapNotNull CreationUploadResult.Empty

                    val creationUploadData = CreationUploadData.parseFromEntity(data, gson)

                    when (creationUploadData.uploadStatus) {
                        CreationUploadStatus.Upload -> {
                            CreationUploadResult.Upload(creationUploadData, creationUploadData.uploadProgress)
                        }
                        CreationUploadStatus.OtherProcess -> {
                            CreationUploadResult.OtherProcess(creationUploadData, creationUploadData.uploadProgress)
                        }
                        CreationUploadStatus.Success -> {
                            CreationUploadResult.Success(creationUploadData)
                        }
                        CreationUploadStatus.Failed -> {
                            CreationUploadResult.Failed(creationUploadData)
                        }
                        else -> {
                            CreationUploadResult.Unknown
                        }
                    }
                } catch (throwable: Throwable) {
                    logger.sendLog(data.toString(), throwable)
                    null
                }
            }
    }

    override suspend fun retry(removedNotificationId: Int) {
        NotificationManagerCompat.from(appContext).cancel(removedNotificationId)

        if (creationUploadQueueRepository.getTopQueue() == null) {
            uploadResultFlow.emit(CreationUploadResult.Empty)
        } else {
            startWorkManager()
        }
    }

    override suspend fun deleteQueueAndChannel(data: CreationUploadData) {
        creationUploadQueueRepository.deleteQueueAndChannel(data)
    }

    override suspend fun removeFailedContentFromQueue(data: CreationUploadData) {
        creationUploadQueueRepository.deleteQueueAndChannel(data)
        retry(data.notificationIdAfterUpload)

        when (data) {
            is CreationUploadData.Post -> {
                deleteMediaPostCacheUseCase(data.mediaList.map { it.path }.toSet())
            }
            else -> {}
        }
    }

    private suspend fun isTopQueueFailed(): Boolean {
        val topQueue = creationUploadQueueRepository.getTopQueue()
        return topQueue != null && topQueue.uploadStatus == CreationUploadStatus.Failed
    }

    private fun startWorkManager() {
        try {
            workManager.enqueueUniqueWork(
                CreationUploadConst.CREATION_UPLOAD_WORKER,
                ExistingWorkPolicy.KEEP,
                CreationUploaderWorker.build()
            )
        } catch (throwable: Throwable) {
            logger.sendLog(throwable)
        }
    }
}
