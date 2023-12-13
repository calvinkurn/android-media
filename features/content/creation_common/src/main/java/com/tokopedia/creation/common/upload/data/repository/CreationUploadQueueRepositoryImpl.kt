package com.tokopedia.creation.common.upload.data.repository

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.data.local.database.CreationUploadQueueDatabase
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.util.logger.CreationUploadLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
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
    private val logger: CreationUploadLogger,
) : CreationUploadQueueRepository {

    override fun observeTopQueue(): Flow<CreationUploadData> {
        return creationUploadQueueDatabase
            .creationUploadQueueDao()
            .observeTopQueue()
            .distinctUntilChanged()
            .mapNotNull { data ->
                try {
                    CreationUploadData.parseFromEntity(data, gson)
                } catch (throwable: Throwable) {
                    logger.sendLog(data.toString(), throwable)
                    null
                }
            }
    }

    override suspend fun insert(data: CreationUploadData) {
        lockAndSwitchContext(dispatchers) {
            creationUploadQueueDatabase.creationUploadQueueDao().insert(data.mapToEntity(gson))
        }
    }

    override suspend fun getTopQueue(): CreationUploadData? {
        return lockAndSwitchContext(dispatchers) {
            val data = creationUploadQueueDatabase.creationUploadQueueDao().getTopQueue()

            if (data != null) {
                CreationUploadData.parseFromEntity(data, gson)
            } else {
                null
            }
        }
    }

    override suspend fun deleteTopQueue() {
        lockAndSwitchContext(dispatchers) {
            creationUploadQueueDatabase.creationUploadQueueDao().deleteTopQueue()
        }
    }

    override suspend fun delete(queueId: Int) {
        lockAndSwitchContext(dispatchers) {
            creationUploadQueueDatabase.creationUploadQueueDao().delete(queueId)
        }
    }

    override suspend fun updateProgress(
        queueId: Int,
        progress: Int,
        uploadStatus: String,
    ) {
        lockAndSwitchContext(dispatchers) {
            creationUploadQueueDatabase
                .creationUploadQueueDao()
                .updateProgress(
                    queueId,
                    progress,
                    uploadStatus
                )
        }
    }

    private suspend fun <T> lockAndSwitchContext(dispatchers: CoroutineDispatchers, onExecute: suspend () -> T): T {
        return mutex.withLock {
            withContext(dispatchers.io) {
                onExecute()
            }
        }
    }
}
