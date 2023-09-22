package com.tokopedia.creation.common.upload.uploader

import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadResult
import kotlinx.coroutines.flow.Flow

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
interface CreationUploader {

    suspend fun upload(data: CreationUploadData)

    fun observe(): Flow<CreationUploadResult>

    fun retry()

    suspend fun deleteFromQueue(queueId: Int)
}
