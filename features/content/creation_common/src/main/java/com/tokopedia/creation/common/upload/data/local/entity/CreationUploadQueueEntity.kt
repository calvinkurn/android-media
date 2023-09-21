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

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "creation_id")
    val creationId: String = "",

    @ColumnInfo(name = "upload_type")
    val uploadType: String = "",

    @ColumnInfo(name = "queue_status")
    val queueStatus: String = "",

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = 0L,

    @ColumnInfo(name = "data")
    val data: String = "",
) {

    data class Shorts(
        @SerializedName("media_uri_list")
        val mediaUriList: List<String> = emptyList(),

        @SerializedName("cover_uri")
        val coverUri: String = "",

        @SerializedName("source_id")
        val sourceId: String = "",

        @SerializedName("author_id")
        val authorId: String = "",

        @SerializedName("author_type")
        val authorType: String = "",
    )

    data class Stories(
        @SerializedName("media_uri_list")
        val mediaUriList: List<String> = emptyList(),

        @SerializedName("cover_uri")
        val coverUri: String = "",

        @SerializedName("source_id")
        val sourceId: String = "",

        @SerializedName("author_id")
        val authorId: String = "",

        @SerializedName("author_type")
        val authorType: String = "",
    )
}
