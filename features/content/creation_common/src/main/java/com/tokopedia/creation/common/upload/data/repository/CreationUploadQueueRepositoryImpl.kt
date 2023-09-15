package com.tokopedia.creation.common.upload.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.data.local.database.CreationUploadQueueDatabase
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.model.CreationUploadQueue
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class CreationUploadQueueRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val creationUploadQueueDatabase: CreationUploadQueueDatabase
) : CreationUploadQueueRepository {

    override suspend fun insert(data: CreationUploadQueue) {
        withContext(dispatchers.io) {
            creationUploadQueueDatabase.creationUploadQueueDao().insert(data.mapToEntity())
        }
    }
}
