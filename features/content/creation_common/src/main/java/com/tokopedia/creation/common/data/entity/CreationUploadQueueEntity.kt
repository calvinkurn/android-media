package com.tokopedia.creation.common.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */

const val CREATION_UPLOAD_QUEUE = "creation_upload_queue"
@Entity(tableName = CREATION_UPLOAD_QUEUE)
data class CreationUploadQueueEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "upload_type")
    val uploadType: String = "",

    @ColumnInfo(name = "creation_id")
    val creationId: String = "",

    @ColumnInfo(name = "media_uri")
    val mediaUri: String = "",

    @ColumnInfo(name = "cover_uri")
    val cover_uri: String = "",

    @ColumnInfo(name = "source_id")
    val sourceId: String = "",

    @ColumnInfo(name = "author_id")
    val authorId: String = "",

    @ColumnInfo(name = "author_type")
    val authorType: String = "",

    @ColumnInfo(name = "queue_status")
    val queueStatus: String = "",

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = 0L,
)
