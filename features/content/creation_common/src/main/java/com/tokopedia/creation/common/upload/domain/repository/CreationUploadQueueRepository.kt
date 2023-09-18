package com.tokopedia.creation.common.upload.domain.repository

import com.tokopedia.creation.common.upload.model.CreationUploadQueue

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
interface CreationUploadQueueRepository {

    suspend fun insert(data: CreationUploadQueue)

    suspend fun getTopQueue(): CreationUploadQueue?

    suspend fun delete(data: CreationUploadQueue)
}
