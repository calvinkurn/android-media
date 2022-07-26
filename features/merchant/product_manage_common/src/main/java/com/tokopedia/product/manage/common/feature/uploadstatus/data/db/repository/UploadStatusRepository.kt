package com.tokopedia.product.manage.common.feature.uploadstatus.data.db.repository

import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.entity.UploadStatusEntity
import kotlinx.coroutines.flow.Flow

interface UploadStatusRepository {
    fun getUploadStatus(): Flow<UploadStatusEntity?>
    suspend fun setUploadStatus(uploadStatusEntity: UploadStatusEntity)
    suspend fun clearUploadStatus()
}