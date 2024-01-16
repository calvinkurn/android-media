package com.tokopedia.creation.common.upload.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.creation.common.upload.data.local.entity.CREATION_UPLOAD_QUEUE
import com.tokopedia.creation.common.upload.data.local.entity.CreationUploadQueueEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
@Dao
interface CreationUploadQueueDao {

    @Query(GET_TOP_QUEUE_QUERY)
    fun observeTopQueue(): Flow<CreationUploadQueueEntity?>

    @Query(GET_TOP_QUEUE_QUERY)
    suspend fun getTopQueue(): CreationUploadQueueEntity?

    @Insert
    suspend fun insert(entity: CreationUploadQueueEntity)

    @Query("DELETE FROM $CREATION_UPLOAD_QUEUE WHERE queue_id IN (SELECT queue_id FROM $CREATION_UPLOAD_QUEUE ORDER BY timestamp ASC LIMIT 1)")
    suspend fun deleteTopQueue()

    @Query("DELETE FROM $CREATION_UPLOAD_QUEUE WHERE queue_id = :queueId")
    suspend fun delete(queueId: Int)

    @Query("UPDATE $CREATION_UPLOAD_QUEUE SET upload_progress = :progress, upload_status = :uploadStatus WHERE queue_id = :queueId")
    suspend fun updateProgress(
        queueId: Int,
        progress: Int,
        uploadStatus: String,
    )
}

private const val GET_TOP_QUEUE_QUERY = "SELECT * FROM $CREATION_UPLOAD_QUEUE ORDER BY timestamp ASC LIMIT 1"
