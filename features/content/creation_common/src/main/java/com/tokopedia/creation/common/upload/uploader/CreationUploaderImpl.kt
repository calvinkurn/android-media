package com.tokopedia.creation.common.upload.uploader

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.creation.common.upload.const.CreationUploadConst
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadResult
import com.tokopedia.creation.common.upload.model.CreationUploadStatus
import com.tokopedia.creation.common.upload.uploader.worker.CreationUploaderWorker
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class CreationUploaderImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val workManager: WorkManager,
    private val creationUploadQueueRepository: CreationUploadQueueRepository,
    private val gson: Gson
) : CreationUploader {

    private val workManagerLiveData: LiveData<CreationUploadResult>
        get() = Transformations.map(
            workManager
                .getWorkInfosForUniqueWorkLiveData(CreationUploadConst.CREATION_UPLOAD_WORKER)
        ) {
            it.firstOrNull()?.let { workInfo ->
                if(workInfo.state == WorkInfo.State.RUNNING) {
                    try {
                        val progress = workInfo.progress.getInt(CreationUploadConst.PROGRESS, 0)
                        val uploadData = CreationUploadData.parseFromJson(workInfo.progress.getString(CreationUploadConst.UPLOAD_DATA).orEmpty(), gson)
                        val uploadStatus = CreationUploadStatus.parse(workInfo.progress.getString(CreationUploadConst.UPLOAD_STATUS).orEmpty())

                        return@map when (uploadStatus) {
                            CreationUploadStatus.Upload -> {
                                CreationUploadResult.Upload(uploadData, progress)
                            }
                            CreationUploadStatus.Success -> {
                                CreationUploadResult.Success(uploadData)
                            }
                            CreationUploadStatus.Failed -> {
                                CreationUploadResult.Failed(uploadData)
                            }
                            CreationUploadStatus.OtherProcess -> {
                                CreationUploadResult.OtherProcess(uploadData, progress)
                            }
                            else -> {
                                CreationUploadResult.Unknown
                            }
                        }
                    } catch (throwable: Throwable) {

                    }
                }
            }

            return@map CreationUploadResult.Unknown
        }

    private val uploadResultFlow = MutableSharedFlow<CreationUploadResult>()

    override suspend fun upload(data: CreationUploadData) {
        creationUploadQueueRepository.insert(data)
        startWorkManager()
    }

    override fun observe(): Flow<CreationUploadResult> {
        return callbackFlow {

            val creationUploadData = creationUploadQueueRepository.getTopQueue()

            if (creationUploadData != null) {
                when (creationUploadData.uploadStatus) {
                    CreationUploadStatus.Upload -> {
                        trySendBlocking(CreationUploadResult.Upload(creationUploadData, creationUploadData.uploadProgress))
                    }
                    CreationUploadStatus.OtherProcess -> {
                        trySendBlocking(CreationUploadResult.OtherProcess(creationUploadData, creationUploadData.uploadProgress))
                    }
                    CreationUploadStatus.Success -> {
                        trySendBlocking(CreationUploadResult.Success(creationUploadData))
                    }
                    CreationUploadStatus.Failed -> {
                        trySendBlocking(CreationUploadResult.Failed(creationUploadData))
                    }
                    else -> {}
                }
            }

            val observer = Observer<CreationUploadResult> {
                trySendBlocking(it)
            }

            workManagerLiveData.observeForever(observer)

            launch {
                uploadResultFlow.collect {
                    trySendBlocking(it)
                }
            }

            awaitClose {
                workManagerLiveData.removeObserver(observer)
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

    override suspend fun deleteTopQueue() {
        creationUploadQueueRepository.deleteTopQueue()
    }

    override suspend fun deleteFromQueue(queueId: Int) {
        creationUploadQueueRepository.delete(queueId)
    }

    private fun startWorkManager() {
        workManager.enqueueUniqueWork(
            CreationUploadConst.CREATION_UPLOAD_WORKER,
            ExistingWorkPolicy.KEEP,
            CreationUploaderWorker.build()
        )
    }
}
