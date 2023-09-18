package com.tokopedia.creation.common.upload.uploader

import com.tokopedia.creation.common.upload.model.CreationUploadQueue

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
interface CreationUploader {

    suspend fun upload(data: CreationUploadQueue)

    suspend fun deleteFromQueue(creationId: String)
}
