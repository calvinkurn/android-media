package com.tokopedia.creation.common.upload.model

import com.tokopedia.creation.common.upload.data.local.entity.CreationUploadQueueEntity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel
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

    val notificationId: Int
        get() = creationId.toIntOrZero()

    /**
     * Need to differentiate notification Id between in progress & success / error
     * since [notificationId] is already set for Foreground Work and will be dismissed
     * automatically when the worker is done.
     */
    val notificationIdAfterUpload: Int
        get() = notificationId + 1

    override fun toString(): String {
        return mapOf(
            KEY_ID to id,
            KEY_UPLOAD_TYPE to uploadType.type,
            KEY_QUEUE_STATUS to queueStatus.value,
            KEY_TIMESTAMP to timestamp,
            KEY_CREATION_ID to creationId,
            KEY_MEDIA_URI to mediaUri,
            KEY_COVER_URI to coverUri,
            KEY_SOURCE_ID to sourceId,
            KEY_AUTHOR_ID to authorId,
            KEY_AUTHOR_TYPE to authorType,
        ).toString()
    }

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

        private const val KEY_ID = "KEY_ID"
        private const val KEY_UPLOAD_TYPE = "KEY_UPLOAD_TYPE"
        private const val KEY_QUEUE_STATUS = "KEY_QUEUE_STATUS"
        private const val KEY_TIMESTAMP = "KEY_TIMESTAMP"
        private const val KEY_CREATION_ID = "KEY_CREATION_ID"
        private const val KEY_AUTHOR_ID = "KEY_AUTHOR_ID"
        private const val KEY_AUTHOR_TYPE = "KEY_AUTHOR_TYPE"
        private const val KEY_MEDIA_URI = "KEY_MEDIA_URI"
        private const val KEY_COVER_URI = "KEY_COVER_URI"
        private const val KEY_SOURCE_ID = "KEY_SOURCE_ID"

        private const val OPEN_BRACKET = "{"
        private const val CLOSE_BRACKET = "}"
        private const val ELEMENT_SEPARATOR = ", "
        private const val KEY_VALUE_SEPARATOR = "="

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

        fun parse(rawData: String): CreationUploadQueue {
            return try {
                val map: Map<String, Any> = if (rawData.isEmpty()) {
                    mapOf()
                } else {
                    rawData
                        .replace(OPEN_BRACKET, "")
                        .replace(CLOSE_BRACKET, "")
                        .split(ELEMENT_SEPARATOR)
                        .associate {
                            val (key, value) = it.split(KEY_VALUE_SEPARATOR)
                            key to value
                        }
                }

                CreationUploadQueue(
                    id = (map[KEY_ID] as? String).orEmpty(),
                    uploadType = CreationUploadType.mapFromValue((map[KEY_UPLOAD_TYPE] as? String).orEmpty()),
                    queueStatus = UploadQueueStatus.mapFromValue((map[KEY_QUEUE_STATUS] as? String).orEmpty()),
                    timestamp = (map[KEY_TIMESTAMP] as? Long).orZero(),
                    creationId = (map[KEY_CREATION_ID] as? String).orEmpty(),
                    authorId = (map[KEY_AUTHOR_ID] as? String).orEmpty(),
                    authorType = (map[KEY_AUTHOR_TYPE] as? String).orEmpty(),
                    mediaUri = (map[KEY_MEDIA_URI] as? String).orEmpty(),
                    coverUri = (map[KEY_COVER_URI] as? String).orEmpty(),
                    sourceId = (map[KEY_SOURCE_ID] as? String).orEmpty()
                )
            } catch (e: Exception) {
                Empty
            }
        }

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

fun CreationUploadQueue?.orEmpty() = this ?: CreationUploadQueue.Empty
