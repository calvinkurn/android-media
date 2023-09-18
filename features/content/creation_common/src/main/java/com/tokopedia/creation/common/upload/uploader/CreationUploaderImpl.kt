package com.tokopedia.creation.common.upload.uploader

import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.tokopedia.creation.common.upload.const.CreationUploadConst
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.model.CreationUploadQueue
import com.tokopedia.creation.common.upload.uploader.worker.CreationUploaderWorker
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class CreationUploaderImpl @Inject constructor(
    private val workManager: WorkManager,
    private val creationUploadQueueRepository: CreationUploadQueueRepository
) : CreationUploader {

    override suspend fun upload(data: CreationUploadQueue) {
        creationUploadQueueRepository.insert(data)

        workManager.enqueueUniqueWork(
            CreationUploadConst.CREATION_UPLOAD_WORKER,
            ExistingWorkPolicy.KEEP,
            CreationUploaderWorker.build()
        )
    }

    override suspend fun deleteFromQueue(creationId: String) {
        creationUploadQueueRepository.delete(creationId)
    }
}
