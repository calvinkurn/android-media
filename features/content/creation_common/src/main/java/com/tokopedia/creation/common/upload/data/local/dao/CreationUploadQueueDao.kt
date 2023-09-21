package com.tokopedia.creation.common.upload.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.creation.common.upload.data.local.entity.CREATION_UPLOAD_QUEUE
import com.tokopedia.creation.common.upload.data.local.entity.CreationUploadQueueEntity

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
@Dao
interface CreationUploadQueueDao {

    @Query("SELECT * FROM $CREATION_UPLOAD_QUEUE ORDER BY timestamp DESC LIMIT 1")
    suspend fun getTopQueue(): CreationUploadQueueEntity?

    @Insert
    suspend fun insert(entity: CreationUploadQueueEntity)

    @Query("DELETE FROM $CREATION_UPLOAD_QUEUE WHERE creation_id IN (SELECT creation_id FROM $CREATION_UPLOAD_QUEUE ORDER BY timestamp DESC LIMIT 1)")
    suspend fun deleteTopQueue()

    @Query("DELETE FROM $CREATION_UPLOAD_QUEUE WHERE creation_id = :creationId")
    suspend fun delete(creationId: String)
}
