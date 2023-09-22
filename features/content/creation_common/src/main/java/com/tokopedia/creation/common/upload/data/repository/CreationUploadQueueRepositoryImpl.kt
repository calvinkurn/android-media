package com.tokopedia.creation.common.upload.data.repository

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.data.local.database.CreationUploadQueueDatabase
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.model.CreationUploadData
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
    private val gson: Gson,
    private val creationUploadQueueDatabase: CreationUploadQueueDatabase,
) : CreationUploadQueueRepository {

    override suspend fun insert(data: CreationUploadData) {
        mutex.withLock {
            withContext(dispatchers.io) {
                creationUploadQueueDatabase.creationUploadQueueDao().insert(data.mapToEntity(gson))
            }
        }
    }

    override suspend fun getTopQueue(): CreationUploadData? {
        return mutex.withLock {
            withContext(dispatchers.io) {
                val data = creationUploadQueueDatabase.creationUploadQueueDao().getTopQueue()

                if (data != null) {
                    CreationUploadData.parseFromEntity(data, gson)
                } else {
                    null
                }
            }
        }
    }

    override suspend fun deleteTopQueue() {
        mutex.withLock {
            withContext(dispatchers.io) {
                creationUploadQueueDatabase.creationUploadQueueDao().deleteTopQueue()
            }
        }
    }

    override suspend fun delete(queueId: Int) {
        mutex.withLock {
            withContext(dispatchers.io) {
                creationUploadQueueDatabase.creationUploadQueueDao().delete(queueId)
            }
        }
    }
}
