package com.tokopedia.creation.common.upload.data.local.entity

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */

const val CREATION_UPLOAD_QUEUE = "creation_upload_queue"
@Entity(tableName = CREATION_UPLOAD_QUEUE)
data class CreationUploadQueueEntity(

    @SuppressLint("Invalid Data Type")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "queue_id")
    @SerializedName("queue_id")
    val queueId: Int = 0,

    @ColumnInfo(name = "creation_id")
    @SerializedName("creation_id")
    val creationId: String = "",

    @ColumnInfo(name = "upload_type")
    @SerializedName("upload_type")
    val uploadType: String = "",

    @ColumnInfo(name = "upload_progress")
    @SerializedName("upload_progress")
    val uploadProgress: Int = 0,

    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    val uploadStatus: String = "",

    @ColumnInfo(name = "timestamp")
    @SerializedName("timestamp")
    val timestamp: Long = 0L,

    @ColumnInfo(name = "cover_uri")
    @SerializedName("cover_uri")
    val coverUri: String = "",

    @ColumnInfo(name = "author_id")
    @SerializedName("author_id")
    val authorId: String = "",

    @ColumnInfo(name = "author_type")
    @SerializedName("author_type")
    val authorType: String = "",

    @ColumnInfo(name = "data")
    @SerializedName("data")
    val data: String = "",
) {

    data class Post(
        @SerializedName("draft_id")
        val draftId: String = "",

        @SerializedName("activity_id")
        val activityId: String = "",
    )

    data class Shorts(
        @SerializedName("media_uri_list")
        val mediaUriList: List<String> = emptyList(),

        @SerializedName("source_id")
        val sourceId: String = "",
    )

    data class Stories(
        @SerializedName("media_uri_list")
        val mediaUriList: List<String> = emptyList(),

        @SerializedName("media_type_list")
        val mediaTypeList: List<Int> = emptyList(),

        @SerializedName("image_source_id")
        val imageSourceId: String = "",

        @SerializedName("video_source_id")
        val videoSourceId: String = "",

        @SerializedName("applink")
        val applink: String = "",
    )
}
