package com.tokopedia.creation.common.upload.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.tokopedia.creation.common.upload.data.local.entity.CreationUploadQueueEntity
import com.tokopedia.creation.common.upload.model.exception.UnknownUploadTypeException
import com.tokopedia.kotlin.extensions.view.orZero

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
sealed interface CreationUploadData {
    val queueId: Int
    val uploadType: CreationUploadType
    val uploadProgress: Int
    val uploadStatus: CreationUploadStatus
    val timestamp: Long
    val creationId: String
    val coverUri: String
    val authorId: String
    val authorType: String

    val notificationCover: String

    val notificationId: Int
        get() = queueId

    /**
     * Need to differentiate notification Id between in progress & success / error
     * since [notificationId] is already set for Foreground Work and will be dismissed
     * automatically when the worker is done.
     */
    val notificationIdAfterUpload: Int
        get() = notificationId + 1

    fun mapToEntity(gson: Gson): CreationUploadQueueEntity

    fun mapDataToJson(gson: Gson): String

    fun mapToJson(gson: Gson): String {
        return try {
            gson.toJson(mapToEntity(gson))
        } catch (_: Throwable) {
            ""
        }
    }

    data class Post(
        @SerializedName(KEY_QUEUE_ID)
        override val queueId: Int,

        @SerializedName(KEY_UPLOAD_TYPE)
        override val uploadType: CreationUploadType,

        @SerializedName(KEY_UPLOAD_PROGRESS)
        override val uploadProgress: Int,

        @SerializedName(KEY_UPLOAD_STATUS)
        override val uploadStatus: CreationUploadStatus,

        @SerializedName(KEY_TIMESTAMP)
        override val timestamp: Long,

        @SerializedName(KEY_CREATION_ID)
        override val creationId: String,

        @SerializedName(KEY_COVER_URI)
        override val coverUri: String,

        @SerializedName(KEY_AUTHOR_ID)
        override val authorId: String,

        @SerializedName(KEY_AUTHOR_TYPE)
        override val authorType: String,

        @SerializedName(KEY_ACTIVITY_ID)
        val activityId: String,

        @SerializedName("token")
        val token: String,

        @SerializedName("caption")
        val caption: String,

        @SerializedName("mediaWidth")
        val mediaWidth: Int,

        @SerializedName("mediaHeight")
        val mediaHeight: Int,

        @SerializedName("mediaList")
        val mediaList: List<Media>,
    ) : CreationUploadData {

        data class Media(
            @SerializedName("path")
            val path: String,

            @SerializedName("type")
            val type: String,

            @SerializedName("productIds")
            val productIds: List<String>,
        )

        override val notificationCover: String
            get() = coverUri

        override fun mapToEntity(gson: Gson): CreationUploadQueueEntity {
            return CreationUploadQueueEntity(
                queueId = queueId,
                creationId = creationId,
                uploadType = uploadType.type,
                uploadProgress = uploadProgress,
                uploadStatus = uploadStatus.value,
                timestamp = timestamp,
                coverUri = coverUri,
                authorId = authorId,
                authorType = authorType,
                data = mapDataToJson(gson),
            )
        }

        override fun mapDataToJson(gson: Gson): String {
            return gson.toJson(
                CreationUploadQueueEntity.Post(
                    activityId = activityId,
                    token = token,
                    caption = caption,
                    mediaWidth = mediaWidth,
                    mediaHeight = mediaHeight,
                    mediaList = mediaList.map { media ->
                        CreationUploadQueueEntity.Post.Media(
                            path = media.path,
                            type = media.type,
                            productIds = media.productIds
                        )
                    }
                )
            )
        }
    }

    data class Shorts(
        @SerializedName(KEY_QUEUE_ID)
        override val queueId: Int,

        @SerializedName(KEY_UPLOAD_TYPE)
        override val uploadType: CreationUploadType,

        @SerializedName(KEY_UPLOAD_PROGRESS)
        override val uploadProgress: Int,

        @SerializedName(KEY_UPLOAD_STATUS)
        override val uploadStatus: CreationUploadStatus,

        @SerializedName(KEY_TIMESTAMP)
        override val timestamp: Long,

        @SerializedName(KEY_CREATION_ID)
        override val creationId: String,

        @SerializedName(KEY_COVER_URI)
        override val coverUri: String,

        @SerializedName(KEY_AUTHOR_ID)
        override val authorId: String,

        @SerializedName(KEY_AUTHOR_TYPE)
        override val authorType: String,

        @SerializedName(KEY_MEDIA_URI_LIST)
        val mediaUriList: List<String>,

        @SerializedName(KEY_SOURCE_ID)
        val sourceId: String,

        @SerializedName(KEY_IS_INTERSPERSED)
        val isInterspersed: Boolean,
    ) : CreationUploadData {

        val firstMediaUri: String
            get() = mediaUriList.firstOrNull().orEmpty()

        override val notificationCover: String
            get() = coverUri.ifEmpty { firstMediaUri }

        override fun mapToEntity(gson: Gson): CreationUploadQueueEntity {
            return CreationUploadQueueEntity(
                queueId = queueId,
                creationId = creationId,
                uploadType = uploadType.type,
                uploadProgress = uploadProgress,
                uploadStatus = uploadStatus.value,
                timestamp = timestamp,
                coverUri = coverUri,
                authorId = authorId,
                authorType = authorType,
                data = mapDataToJson(gson),
            )
        }

        override fun mapDataToJson(gson: Gson): String {
            return gson.toJson(
                CreationUploadQueueEntity.Shorts(
                    mediaUriList = mediaUriList,
                    sourceId = sourceId,
                    isInterspersed = isInterspersed,
                )
            )
        }
    }

    data class Stories(
        @SerializedName(KEY_QUEUE_ID)
        override val queueId: Int,

        @SerializedName(KEY_UPLOAD_TYPE)
        override val uploadType: CreationUploadType,

        @SerializedName(KEY_UPLOAD_PROGRESS)
        override val uploadProgress: Int,

        @SerializedName(KEY_UPLOAD_STATUS)
        override val uploadStatus: CreationUploadStatus,

        @SerializedName(KEY_TIMESTAMP)
        override val timestamp: Long,

        @SerializedName(KEY_CREATION_ID)
        override val creationId: String,

        @SerializedName(KEY_COVER_URI)
        override val coverUri: String,

        @SerializedName(KEY_AUTHOR_ID)
        override val authorId: String,

        @SerializedName(KEY_AUTHOR_TYPE)
        override val authorType: String,

        @SerializedName(KEY_MEDIA_URI_LIST)
        val mediaUriList: List<String>,

        @SerializedName(KEY_MEDIA_TYPE_LIST)
        val mediaTypeList: List<Int>,

        @SerializedName(KEY_IMAGE_SOURCE_ID)
        val imageSourceId: String,

        @SerializedName(KEY_VIDEO_SOURCE_ID)
        val videoSourceId: String,

        @SerializedName(KEY_APPLINK)
        val applink: String,
    ) : CreationUploadData {

        val firstMediaUri: String
            get() = mediaUriList.firstOrNull().orEmpty()

        val firstMediaType: ContentMediaType
            get() = ContentMediaType.parse(mediaTypeList.firstOrNull().orZero())

        override val notificationCover: String
            get() = coverUri.ifEmpty { firstMediaUri }

        override fun mapToEntity(gson: Gson): CreationUploadQueueEntity {
            return CreationUploadQueueEntity(
                queueId = queueId,
                creationId = creationId,
                uploadType = uploadType.type,
                uploadProgress = uploadProgress,
                uploadStatus = uploadStatus.value,
                timestamp = timestamp,
                coverUri = coverUri,
                authorId = authorId,
                authorType = authorType,
                data = mapDataToJson(gson),
            )
        }

        override fun mapDataToJson(gson: Gson): String {
            return gson.toJson(
                CreationUploadQueueEntity.Stories(
                    mediaUriList = mediaUriList,
                    mediaTypeList = mediaTypeList,
                    imageSourceId = imageSourceId,
                    videoSourceId = videoSourceId,
                    applink = applink,
                )
            )
        }
    }

    companion object {

        private const val KEY_QUEUE_ID = "queueId"
        private const val KEY_UPLOAD_TYPE = "uploadType"
        private const val KEY_UPLOAD_PROGRESS = "uploadProgress"
        private const val KEY_UPLOAD_STATUS = "uploadStatus"
        private const val KEY_TIMESTAMP = "timestamp"
        private const val KEY_CREATION_ID = "creationId"
        private const val KEY_AUTHOR_ID = "authorId"
        private const val KEY_AUTHOR_TYPE = "authorType"
        private const val KEY_MEDIA_URI_LIST = "mediaUriList"
        private const val KEY_MEDIA_TYPE_LIST = "mediaTypeList"
        private const val KEY_COVER_URI = "coverUri"
        private const val KEY_SOURCE_ID = "sourceId"
        private const val KEY_IMAGE_SOURCE_ID = "image_source_id"
        private const val KEY_VIDEO_SOURCE_ID = "video_source_id"
        private const val KEY_ACTIVITY_ID = "activityId"
        private const val KEY_APPLINK = "applink"
        private const val KEY_IS_INTERSPERSED = "is_interspersed"

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

            val uploadStatus = CreationUploadStatus.parse(entity.uploadStatus)

            return when (val uploadType = CreationUploadType.mapFromValue(entity.uploadType)) {
                CreationUploadType.Post -> {
                    val postEntity = gson.fromJson<CreationUploadQueueEntity.Post>(
                        entity.data,
                        object : TypeToken<CreationUploadQueueEntity.Post>(){}.type
                    )

                    Post(
                        queueId = entity.queueId,
                        creationId = entity.creationId,
                        uploadType = uploadType,
                        uploadProgress = entity.uploadProgress,
                        uploadStatus = uploadStatus,
                        timestamp = entity.timestamp,
                        coverUri = entity.coverUri,
                        authorId = entity.authorId,
                        authorType = entity.authorType,
                        activityId = postEntity.activityId,
                        token = postEntity.token,
                        caption = postEntity.caption,
                        mediaWidth = postEntity.mediaWidth,
                        mediaHeight = postEntity.mediaHeight,
                        mediaList = postEntity.mediaList.map { media ->
                            Post.Media(
                                path = media.path,
                                type = media.type,
                                productIds = media.productIds,
                            )
                        }
                    )
                }
                CreationUploadType.Shorts -> {
                    val shortsEntity = gson.fromJson<CreationUploadQueueEntity.Shorts>(
                        entity.data,
                        object : TypeToken<CreationUploadQueueEntity.Shorts>(){}.type
                    )

                    Shorts(
                        queueId = entity.queueId,
                        creationId = entity.creationId,
                        uploadType = uploadType,
                        uploadProgress = entity.uploadProgress,
                        uploadStatus = uploadStatus,
                        timestamp = entity.timestamp,
                        coverUri = entity.coverUri,
                        authorId = entity.authorId,
                        authorType = entity.authorType,
                        mediaUriList = shortsEntity.mediaUriList,
                        sourceId = shortsEntity.sourceId,
                        isInterspersed = shortsEntity.isInterspersed,
                    )
                }
                CreationUploadType.Stories ->  {
                    val storiesEntity = gson.fromJson<CreationUploadQueueEntity.Stories>(
                        entity.data,
                        object : TypeToken<CreationUploadQueueEntity.Stories>(){}.type
                    )

                    Stories(
                        queueId = entity.queueId,
                        creationId = entity.creationId,
                        uploadType = uploadType,
                        uploadProgress = entity.uploadProgress,
                        uploadStatus = uploadStatus,
                        timestamp = entity.timestamp,
                        coverUri = entity.coverUri,
                        authorId = entity.authorId,
                        authorType = entity.authorType,
                        mediaUriList = storiesEntity.mediaUriList,
                        mediaTypeList = storiesEntity.mediaTypeList,
                        imageSourceId = storiesEntity.imageSourceId,
                        videoSourceId = storiesEntity.videoSourceId,
                        applink = storiesEntity.applink,
                    )
                }
                else -> throw UnknownUploadTypeException()
            }
        }

        fun buildForPost(
            creationId: String,
            coverUri: String,
            authorId: String,
            authorType: String,
            token: String,
            caption: String,
            mediaWidth: Int,
            mediaHeight: Int,
            mediaList: List<Post.Media>,
        ): CreationUploadData {
            return Post(
                queueId = 0,
                uploadType = CreationUploadType.Post,
                uploadProgress = 0,
                uploadStatus = CreationUploadStatus.Unknown,
                timestamp = System.currentTimeMillis(),
                creationId = creationId,
                coverUri = coverUri,
                authorId = authorId,
                authorType = authorType,
                activityId = "",
                token = token,
                caption = caption,
                mediaWidth = mediaWidth,
                mediaHeight = mediaHeight,
                mediaList = mediaList,
            )
        }

        fun buildForShorts(
            creationId: String,
            mediaUriList: List<String>,
            coverUri: String,
            sourceId: String,
            authorId: String,
            authorType: String,
            isInterspersed: Boolean,
        ): CreationUploadData {

            return Shorts(
                queueId = 0,
                uploadType = CreationUploadType.Shorts,
                uploadProgress = 0,
                uploadStatus = CreationUploadStatus.Unknown,
                timestamp = System.currentTimeMillis(),
                creationId = creationId,
                mediaUriList = mediaUriList,
                coverUri = coverUri,
                sourceId = sourceId,
                authorId = authorId,
                authorType = authorType,
                isInterspersed = isInterspersed,
            )
        }

        fun buildForStories(
            creationId: String,
            mediaUriList: List<String>,
            mediaTypeList: List<Int>,
            coverUri: String,
            authorId: String,
            authorType: String,
            imageSourceId: String,
            videoSourceId: String,
            applink: String,
        ): CreationUploadData {
            return Stories(
                queueId = 0,
                uploadType = CreationUploadType.Stories,
                uploadProgress = 0,
                uploadStatus = CreationUploadStatus.Unknown,
                timestamp = System.currentTimeMillis(),
                creationId = creationId,
                mediaUriList = mediaUriList,
                mediaTypeList = mediaTypeList,
                coverUri = coverUri,
                authorId = authorId,
                authorType = authorType,
                imageSourceId = imageSourceId,
                videoSourceId = videoSourceId,
                applink = applink,
            )
        }
    }
}
