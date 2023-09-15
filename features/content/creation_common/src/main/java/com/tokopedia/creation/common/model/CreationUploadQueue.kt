package com.tokopedia.creation.common.model


/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
data class CreationUploadQueue(
    val id: String,
    val uploadType: CreationUploadType,
    val creationId: String,
    val mediaUri: String,
    val cover_uri: String,
    val sourceId: String,
    val authorId: String,
    val authorType: String,
    val queueStatus: UploadQueueStatus,
    val timestamp: Long = 0L,
)
