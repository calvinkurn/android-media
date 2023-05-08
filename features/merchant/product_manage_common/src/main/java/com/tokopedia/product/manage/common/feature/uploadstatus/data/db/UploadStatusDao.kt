package com.tokopedia.product.manage.common.feature.uploadstatus.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.entity.UploadStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UploadStatusDao {

    @Query(value = "SELECT * FROM upload_status ORDER BY id DESC LIMIT 1")
    fun getUploadStatus(): Flow<UploadStatusEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun setUploadStatus(uploadStatusDao: UploadStatusEntity)

    @Query(value = "DELETE FROM upload_status")
    suspend fun clearUploadStatus()

}
