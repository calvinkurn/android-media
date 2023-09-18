package com.tokopedia.creation.common.upload.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.data.local.database.CreationUploadQueueDatabase
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.model.CreationUploadQueue
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class CreationUploadQueueRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val mutex: Mutex,
    private val creationUploadQueueDatabase: CreationUploadQueueDatabase
) : CreationUploadQueueRepository {

    override suspend fun insert(data: CreationUploadQueue) {
        mutex.withLock {
            withContext(dispatchers.io) {
                creationUploadQueueDatabase.creationUploadQueueDao().insert(data.mapToEntity())
            }
        }
    }

    override suspend fun getTopQueue(): CreationUploadQueue? {
        return mutex.withLock {
            withContext(dispatchers.io) {
                val data = creationUploadQueueDatabase.creationUploadQueueDao().getTopQueue()

                if (data != null) {
                    CreationUploadQueue.parseFromEntity(data)
                } else {
                    null
                }
            }
        }
    }

    override suspend fun delete(data: CreationUploadQueue) {
        return mutex.withLock {
            withContext(dispatchers.io) {
                creationUploadQueueDatabase.creationUploadQueueDao().delete(data.creationId)
            }
        }
    }
}
