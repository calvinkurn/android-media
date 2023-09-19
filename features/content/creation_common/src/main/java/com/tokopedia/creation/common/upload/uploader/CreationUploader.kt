package com.tokopedia.creation.common.upload.uploader

import com.tokopedia.creation.common.upload.model.CreationUploadData

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
interface CreationUploader {

    suspend fun upload(data: CreationUploadData)

    fun retry()

    suspend fun deleteFromQueue(creationId: String)
}
