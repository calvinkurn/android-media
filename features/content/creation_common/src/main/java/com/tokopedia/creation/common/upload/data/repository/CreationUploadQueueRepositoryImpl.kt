package com.tokopedia.creation.common.upload.data.repository

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.data.local.database.CreationUploadQueueDatabase
import com.tokopedia.creation.common.upload.data.local.entity.CreationUploadQueueEntity
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.domain.usecase.stories.StoriesUpdateStoryUseCase
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadType
import com.tokopedia.creation.common.upload.model.dto.stories.StoriesUpdateStoryRequest
import com.tokopedia.creation.common.upload.model.stories.StoriesStatus
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.domain.usecase.broadcaster.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.CacheUtil
import kotlinx.coroutines.flow.Flow
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
    private val updateShortsChannelUseCase: PlayBroadcastUpdateChannelUseCase,
    private val updateStoryUseCase: StoriesUpdateStoryUseCase,
) : CreationUploadQueueRepository {

    override fun observeTopQueue(): Flow<CreationUploadQueueEntity?> {
        return creationUploadQueueDatabase
            .creationUploadQueueDao()
            .observeTopQueue()
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
            val topQueue = creationUploadQueueDatabase.creationUploadQueueDao().getTopQueue()
            creationUploadQueueDatabase.creationUploadQueueDao().deleteTopQueue()

            if (topQueue != null) {
                deleteMediaCache(topQueue)
            }
        }
    }

    override suspend fun delete(data: CreationUploadData) {
        lockAndSwitchContext(dispatchers) {
            try {
                creationUploadQueueDatabase.creationUploadQueueDao().delete(data.queueId)

                when (data) {
                    is CreationUploadData.Stories -> {
                        CacheUtil.deleteFileFromCache(data.firstMediaUri)
                    }
                    else -> {}
                }
            } catch (_: Throwable) {

            }
        }
    }

    override suspend fun deleteQueueAndChannel(data: CreationUploadData) {
        delete(data)

        withContext(dispatchers.io) {
            try {
                when (data.uploadType) {
                    CreationUploadType.Shorts -> {
                        updateShortsChannelUseCase.apply {
                            setQueryParams(
                                UpdateChannelUseCase.createUpdateStatusRequest(
                                    channelId = data.creationId,
                                    authorId = data.authorId,
                                    status = PlayChannelStatusType.Deleted
                                )
                            )
                        }.executeOnBackground()
                    }
                    CreationUploadType.Stories -> {
                        updateStoryUseCase(
                            StoriesUpdateStoryRequest.create(
                                storyId = data.creationId,
                                activeMediaId = "0",
                                status = StoriesStatus.Deleted,
                            )
                        )
                    }
                    else -> {}
                }
            } catch (_: Throwable) {

            }
        }
    }

    override suspend fun clearQueue() {
        lockAndSwitchContext(dispatchers) {
            val queueList = creationUploadQueueDatabase.creationUploadQueueDao().getAllQueue()
            creationUploadQueueDatabase.creationUploadQueueDao().deleteAll()

            queueList
                .filter { it.uploadType == CreationUploadType.Stories.type }
                .forEach { queueEntity ->
                    deleteMediaCache(queueEntity)
                }
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

    override suspend fun updateData(queueId: Int, data: String) {
        lockAndSwitchContext(dispatchers) {
            creationUploadQueueDatabase
                .creationUploadQueueDao()
                .updateData(
                    queueId,
                    data,
                )
        }
    }

    private fun deleteMediaCache(queueEntity: CreationUploadQueueEntity) {

        if (queueEntity.uploadType != CreationUploadType.Stories.type) return

        val stories = CreationUploadData.parseFromEntity(queueEntity, gson) as? CreationUploadData.Stories

        stories?.let {
            CacheUtil.deleteFileFromCache(it.firstMediaUri)
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
