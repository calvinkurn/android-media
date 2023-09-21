package com.tokopedia.creation.common.upload.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.tokopedia.creation.common.upload.data.local.entity.CreationUploadQueueEntity
import com.tokopedia.creation.common.upload.model.exception.UnknownUploadTypeException
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import java.util.UUID

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
sealed interface CreationUploadData {
    val id: String
    val uploadType: CreationUploadType
    val queueStatus: UploadQueueStatus
    val timestamp: Long
    val creationId: String
    val mediaUriList: List<String>
    val coverUri: String
    val sourceId: String
    val authorId: String
    val authorType: String

    val firstMediaUri: String
        get() = mediaUriList.firstOrNull().orEmpty()

    val notificationId: Int
        get() = creationId.toIntOrZero()

    /**
     * Need to differentiate notification Id between in progress & success / error
     * since [notificationId] is already set for Foreground Work and will be dismissed
     * automatically when the worker is done.
     */
    val notificationIdAfterUpload: Int
        get() = notificationId + 1

    fun mapToEntity(gson: Gson): CreationUploadQueueEntity

    fun mapToJson(gson: Gson): String {
        return try {
            gson.toJson(mapToEntity(gson))
        } catch (throwable: Throwable) {
            ""
        }
    }

    data class Post(
        @SerializedName(KEY_ID)
        override val id: String,

        @SerializedName(KEY_UPLOAD_TYPE)
        override val uploadType: CreationUploadType,

        @SerializedName(KEY_QUEUE_STATUS)
        override val queueStatus: UploadQueueStatus,

        @SerializedName(KEY_TIMESTAMP)
        override val timestamp: Long,

        @SerializedName(KEY_CREATION_ID)
        override val creationId: String,

        @SerializedName(KEY_MEDIA_URI_LIST)
        override val mediaUriList: List<String>,

        @SerializedName(KEY_COVER_URI)
        override val coverUri: String,

        @SerializedName(KEY_SOURCE_ID)
        override val sourceId: String,

        @SerializedName(KEY_AUTHOR_ID)
        override val authorId: String,

        @SerializedName(KEY_AUTHOR_TYPE)
        override val authorType: String,
    ) : CreationUploadData {

        override fun mapToEntity(gson: Gson): CreationUploadQueueEntity {
            return CreationUploadQueueEntity(
                id = id,
                creationId = creationId,
                uploadType = uploadType.type,
                queueStatus = queueStatus.value,
                timestamp = timestamp,
                data = gson.toJson(
                    CreationUploadQueueEntity.Post(
                        authorId = authorId,
                        authorType = authorType,
                    )
                )
            )
        }
    }

    data class Shorts(
        @SerializedName(KEY_ID)
        override val id: String,

        @SerializedName(KEY_UPLOAD_TYPE)
        override val uploadType: CreationUploadType,

        @SerializedName(KEY_QUEUE_STATUS)
        override val queueStatus: UploadQueueStatus,

        @SerializedName(KEY_TIMESTAMP)
        override val timestamp: Long,

        @SerializedName(KEY_CREATION_ID)
        override val creationId: String,

        @SerializedName(KEY_MEDIA_URI_LIST)
        override val mediaUriList: List<String>,

        @SerializedName(KEY_COVER_URI)
        override val coverUri: String,

        @SerializedName(KEY_SOURCE_ID)
        override val sourceId: String,

        @SerializedName(KEY_AUTHOR_ID)
        override val authorId: String,

        @SerializedName(KEY_AUTHOR_TYPE)
        override val authorType: String,
    ) : CreationUploadData {

        override fun mapToEntity(gson: Gson): CreationUploadQueueEntity {
            return CreationUploadQueueEntity(
                id = id,
                creationId = creationId,
                uploadType = uploadType.type,
                queueStatus = queueStatus.value,
                timestamp = timestamp,
                data = gson.toJson(
                    CreationUploadQueueEntity.Shorts(
                        mediaUriList = mediaUriList,
                        coverUri = coverUri,
                        sourceId = sourceId,
                        authorId = authorId,
                        authorType = authorType,
                    )
                )
            )
        }
    }

    data class Stories(
        @SerializedName(KEY_ID)
        override val id: String,

        @SerializedName(KEY_UPLOAD_TYPE)
        override val uploadType: CreationUploadType,

        @SerializedName(KEY_QUEUE_STATUS)
        override val queueStatus: UploadQueueStatus,

        @SerializedName(KEY_TIMESTAMP)
        override val timestamp: Long,

        @SerializedName(KEY_CREATION_ID)
        override val creationId: String,

        @SerializedName(KEY_MEDIA_URI_LIST)
        override val mediaUriList: List<String>,

        @SerializedName(KEY_COVER_URI)
        override val coverUri: String,

        @SerializedName(KEY_SOURCE_ID)
        override val sourceId: String,

        @SerializedName(KEY_AUTHOR_ID)
        override val authorId: String,

        @SerializedName(KEY_AUTHOR_TYPE)
        override val authorType: String,
    ) : CreationUploadData {

        override fun mapToEntity(gson: Gson): CreationUploadQueueEntity {
            return CreationUploadQueueEntity(
                id = id,
                creationId = creationId,
                uploadType = uploadType.type,
                queueStatus = queueStatus.value,
                timestamp = timestamp,
                data = gson.toJson(
                    CreationUploadQueueEntity.Stories(
                        mediaUriList = mediaUriList,
                        coverUri = coverUri,
                        sourceId = sourceId,
                        authorId = authorId,
                        authorType = authorType,
                    )
                )
            )
        }
    }

    companion object {

        private const val KEY_ID = "id"
        private const val KEY_UPLOAD_TYPE = "uploadType"
        private const val KEY_QUEUE_STATUS = "queueStatus"
        private const val KEY_TIMESTAMP = "timestamp"
        private const val KEY_CREATION_ID = "creationId"
        private const val KEY_AUTHOR_ID = "authorId"
        private const val KEY_AUTHOR_TYPE = "authorType"
        private const val KEY_MEDIA_URI_LIST = "mediaUriList"
        private const val KEY_COVER_URI = "coverUri"
        private const val KEY_SOURCE_ID = "sourceid"

        fun parseFromJson(json: String, gson: Gson): CreationUploadData {
            val uploadDataEntity = gson.fromJson<CreationUploadQueueEntity>(
                json,
                object : TypeToken<CreationUploadQueueEntity>(){}.type
            )

            return parseFromEntity(uploadDataEntity, gson)
        }

        fun parseFromEntity(
            entity: CreationUploadQueueEntity,
            gson: Gson,
        ): CreationUploadData {
            return when (val uploadType = CreationUploadType.mapFromValue(entity.uploadType)) {
                CreationUploadType.Post -> {
                    val postEntity = gson.fromJson<CreationUploadQueueEntity.Post>(
                        entity.data,
                        object : TypeToken<CreationUploadQueueEntity.Post>(){}.type
                    )

                    Post(
                        id = entity.id,
                        creationId = entity.creationId,
                        uploadType = uploadType,
                        queueStatus = UploadQueueStatus.mapFromValue(entity.queueStatus),
                        timestamp = entity.timestamp,
                        mediaUriList = emptyList(),
                        coverUri = "",
                        sourceId = "",
                        authorId = postEntity.authorId,
                        authorType = postEntity.authorType,
                    )
                }
                CreationUploadType.Shorts -> {
                    val shortsEntity = gson.fromJson<CreationUploadQueueEntity.Shorts>(
                        entity.data,
                        object : TypeToken<CreationUploadQueueEntity.Shorts>(){}.type
                    )

                    Shorts(
                        id = entity.id,
                        creationId = entity.creationId,
                        uploadType = uploadType,
                        queueStatus = UploadQueueStatus.mapFromValue(entity.queueStatus),
                        timestamp = entity.timestamp,
                        mediaUriList = shortsEntity.mediaUriList,
                        coverUri = shortsEntity.coverUri,
                        sourceId = shortsEntity.sourceId,
                        authorId = shortsEntity.authorId,
                        authorType = shortsEntity.authorType,
                    )
                }
                CreationUploadType.Stories ->  {
                    val storiesEntity = gson.fromJson<CreationUploadQueueEntity.Stories>(
                        entity.data,
                        object : TypeToken<CreationUploadQueueEntity.Stories>(){}.type
                    )

                    Stories(
                        id = entity.id,
                        creationId = entity.creationId,
                        uploadType = uploadType,
                        queueStatus = UploadQueueStatus.mapFromValue(entity.queueStatus),
                        timestamp = entity.timestamp,
                        mediaUriList = storiesEntity.mediaUriList,
                        coverUri = storiesEntity.coverUri,
                        sourceId = storiesEntity.sourceId,
                        authorId = storiesEntity.authorId,
                        authorType = storiesEntity.authorType,
                    )
                }
                else -> throw UnknownUploadTypeException()
            }
        }

        fun buildForPost(
            creationId: String,
            mediaUriList: List<String>,
            coverUri: String,
            sourceId: String,
            authorId: String,
            authorType: String,
        ): CreationUploadData {
            return Post(
                id = UUID.randomUUID().toString(),
                uploadType = CreationUploadType.Post,
                queueStatus = UploadQueueStatus.Queued,
                timestamp = System.currentTimeMillis(),
                creationId = creationId,
                mediaUriList = mediaUriList,
                coverUri = coverUri,
                sourceId = sourceId,
                authorId = authorId,
                authorType = authorType,
            )
        }

        fun buildForShorts(
            creationId: String,
            mediaUriList: List<String>,
            coverUri: String,
            sourceId: String,
            authorId: String,
            authorType: String,
        ): CreationUploadData {

            return Shorts(
                id = UUID.randomUUID().toString(),
                uploadType = CreationUploadType.Shorts,
                queueStatus = UploadQueueStatus.Queued,
                timestamp = System.currentTimeMillis(),
                creationId = creationId,
                mediaUriList = mediaUriList,
                coverUri = coverUri,
                sourceId = sourceId,
                authorId = authorId,
                authorType = authorType,
            )
        }

        fun buildForStories(
            creationId: String,
            mediaUriList: List<String>,
            coverUri: String,
            sourceId: String,
            authorId: String,
            authorType: String,
        ): CreationUploadData {
            return Stories(
                id = UUID.randomUUID().toString(),
                uploadType = CreationUploadType.Stories,
                queueStatus = UploadQueueStatus.Queued,
                timestamp = System.currentTimeMillis(),
                creationId = creationId,
                mediaUriList = mediaUriList,
                coverUri = coverUri,
                sourceId = sourceId,
                authorId = authorId,
                authorType = authorType,
            )
        }
    }
}
