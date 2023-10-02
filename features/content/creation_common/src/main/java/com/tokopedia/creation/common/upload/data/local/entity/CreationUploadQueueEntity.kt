package com.tokopedia.creation.common.upload.data.local.entity

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

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "queue_id")
    val queueId: Int = 0,

    @ColumnInfo(name = "creation_id")
    val creationId: String = "",

    @ColumnInfo(name = "upload_type")
    val uploadType: String = "",

    @ColumnInfo(name = "upload_progress")
    val uploadProgress: Int = 0,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = 0L,

    @ColumnInfo(name = "cover_uri")
    val coverUri: String = "",

    @ColumnInfo(name = "source_id")
    val sourceId: String = "",

    @ColumnInfo(name = "author_id")
    val authorId: String = "",

    @ColumnInfo(name = "author_type")
    val authorType: String = "",

    @ColumnInfo(name = "data")
    val data: String = "",
) {

    data class Post(
        @SerializedName("draft_id")
        val draftId: String = "",
    )

    data class Shorts(
        @SerializedName("media_uri_list")
        val mediaUriList: List<String> = emptyList(),
    )

    data class Stories(
        @SerializedName("media_uri_list")
        val mediaUriList: List<String> = emptyList(),
    )
}
