package com.tokopedia.creation.common.upload.domain.repository

import com.tokopedia.creation.common.upload.data.local.entity.CreationUploadQueueEntity
import com.tokopedia.creation.common.upload.model.CreationUploadData
import kotlinx.coroutines.flow.Flow

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
interface CreationUploadQueueRepository {

    fun observeTopQueue(): Flow<CreationUploadQueueEntity?>

    suspend fun insert(data: CreationUploadData)

    suspend fun getTopQueue(): CreationUploadData?

    suspend fun deleteTopQueue()

    suspend fun delete(queueId: Int)

    suspend fun updateProgress(queueId: Int, progress: Int, uploadStatus: String)

    suspend fun updateData(queueId: Int, data: String)
}
