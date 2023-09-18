package com.tokopedia.creation.common.upload.model

import com.tokopedia.creation.common.upload.data.local.entity.CreationUploadQueueEntity
import java.util.UUID


/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
data class CreationUploadQueue private constructor(
    val id: String,
    val uploadType: CreationUploadType,
    val queueStatus: UploadQueueStatus,
    val timestamp: Long = 0L,
    val creationId: String,
    val mediaUri: String,
    val coverUri: String,
    val sourceId: String,
    val authorId: String,
    val authorType: String,
) {

    fun mapToEntity(): CreationUploadQueueEntity {
        return CreationUploadQueueEntity(
            id = id,
            uploadType = uploadType.type,
            queueStatus = queueStatus.value,
            timestamp = timestamp,
            creationId = creationId,
            mediaUri = mediaUri,
            coverUri = coverUri,
            sourceId = sourceId,
            authorId = authorId,
            authorType = authorType,
        )
    }

    companion object {

        val Empty: CreationUploadQueue
            get() = CreationUploadQueue(
                id = "",
                uploadType = CreationUploadType.Unknown,
                queueStatus = UploadQueueStatus.Unknown,
                timestamp = 0L,
                creationId = "",
                mediaUri = "",
                coverUri = "",
                sourceId = "",
                authorId = "",
                authorType = "",
            )

        fun parseFromEntity(entity: CreationUploadQueueEntity): CreationUploadQueue {
            return when (CreationUploadType.mapFromValue(entity.uploadType)) {
                CreationUploadType.Shorts -> buildForShorts(
                    creationId = entity.creationId,
                    mediaUri = entity.mediaUri,
                    coverUri = entity.coverUri,
                    sourceId = entity.sourceId,
                    authorId = entity.authorId,
                    authorType = entity.authorType,
                )
                CreationUploadType.Stories -> buildForStories(
                    creationId = entity.creationId,
                    mediaUri = entity.mediaUri,
                    coverUri = entity.coverUri,
                    sourceId = entity.sourceId,
                    authorId = entity.authorId,
                    authorType = entity.authorType,
                )
                CreationUploadType.Unknown -> Empty
            }
        }

        fun buildForShorts(
            creationId: String,
            mediaUri: String,
            coverUri: String,
            sourceId: String,
            authorId: String,
            authorType: String,
        ): CreationUploadQueue {
            return CreationUploadQueue(
                id = UUID.randomUUID().toString(),
                uploadType = CreationUploadType.Shorts,
                queueStatus = UploadQueueStatus.Queued,
                timestamp = System.currentTimeMillis(),
                creationId = creationId,
                mediaUri = mediaUri,
                coverUri = coverUri,
                sourceId = sourceId,
                authorId = authorId,
                authorType = authorType,
            )
        }

        fun buildForStories(
            creationId: String,
            mediaUri: String,
            coverUri: String,
            sourceId: String,
            authorId: String,
            authorType: String,
        ): CreationUploadQueue {
            return CreationUploadQueue(
                id = UUID.randomUUID().toString(),
                uploadType = CreationUploadType.Stories,
                queueStatus = UploadQueueStatus.Queued,
                timestamp = System.currentTimeMillis(),
                creationId = creationId,
                mediaUri = mediaUri,
                coverUri = coverUri,
                sourceId = sourceId,
                authorId = authorId,
                authorType = authorType,
            )
        }
    }
}
