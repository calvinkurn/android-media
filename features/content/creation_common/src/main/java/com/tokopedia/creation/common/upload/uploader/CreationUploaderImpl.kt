package com.tokopedia.creation.common.upload.uploader

import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.tokopedia.creation.common.upload.const.CreationUploadConst
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.uploader.worker.CreationUploaderWorker
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class CreationUploaderImpl @Inject constructor(
    private val workManager: WorkManager,
    private val creationUploadQueueRepository: CreationUploadQueueRepository
) : CreationUploader {

    override suspend fun upload(data: CreationUploadData) {
        creationUploadQueueRepository.insert(data)
        startWorkManager()
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
