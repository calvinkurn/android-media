package com.tokopedia.creation.common.upload.uploader

import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson
import com.tokopedia.creation.common.upload.const.CreationUploadConst
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadResult
import com.tokopedia.creation.common.upload.uploader.worker.CreationUploaderWorker
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class CreationUploaderImpl @Inject constructor(
    private val workManager: WorkManager,
    private val creationUploadQueueRepository: CreationUploadQueueRepository,
    private val gson: Gson,
) : CreationUploader {

    private val uploadLiveData = Transformations.map(
        workManager
            .getWorkInfosForUniqueWorkLiveData(CreationUploadConst.CREATION_UPLOAD_WORKER)
    ) {
        it.firstOrNull()?.let { workInfo ->
            if(workInfo.state == WorkInfo.State.RUNNING) {
                try {
                    val progress = workInfo.progress.getInt(CreationUploadConst.PROGRESS, 0)
                    val uploadData = CreationUploadData.parseFromJson(workInfo.progress.getString(CreationUploadConst.UPLOAD_DATA).orEmpty(), gson)

                    return@map when (progress) {
                        CreationUploadConst.PROGRESS_COMPLETED -> {
                            CreationUploadResult.Success(uploadData)
                        }
                        CreationUploadConst.PROGRESS_FAILED -> {
                            CreationUploadResult.Failed(uploadData)
                        }
                        else -> {
                            CreationUploadResult.Progress(uploadData, progress)
                        }
                    }
                } catch (throwable: Throwable) {

                }
            }
        }

        return@map CreationUploadResult.Unknown
    }

    override suspend fun upload(data: CreationUploadData) {
        creationUploadQueueRepository.insert(data)
        startWorkManager()
    }

    override fun observe(): Flow<CreationUploadResult> {
        return callbackFlow {
            val observer = Observer<CreationUploadResult> {
                trySendBlocking(it)
            }

            uploadLiveData.observeForever(observer)

            awaitClose {
                uploadLiveData.removeObserver(observer)
            }
        }
    }

    override fun retry() {
        startWorkManager()
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
