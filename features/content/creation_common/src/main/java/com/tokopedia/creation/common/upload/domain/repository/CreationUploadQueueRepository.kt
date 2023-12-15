package com.tokopedia.creation.common.upload.domain.repository

import com.tokopedia.creation.common.upload.model.CreationUploadData

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
interface CreationUploadQueueRepository {

    suspend fun insert(data: CreationUploadData)

    suspend fun getTopQueue(): CreationUploadData?

    suspend fun deleteTopQueue()

    suspend fun delete(queueId: Int)

    suspend fun updateProgress(queueId: Int, progress: Int, uploadStatus: String)
}
