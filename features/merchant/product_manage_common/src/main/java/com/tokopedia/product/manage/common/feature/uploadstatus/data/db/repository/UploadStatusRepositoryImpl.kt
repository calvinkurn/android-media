package com.tokopedia.product.manage.common.feature.uploadstatus.data.db.repository

import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.UploadStatusDao
import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.entity.UploadStatusEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UploadStatusRepositoryImpl @Inject constructor(private val dao: UploadStatusDao): UploadStatusRepository {

    override fun getUploadStatus(): Flow<UploadStatusEntity?> {
        return dao.getUploadStatus()
    }

    override suspend fun setUploadStatus(uploadStatusEntity: UploadStatusEntity) {
        dao.setUploadStatus(uploadStatusEntity)
    }

    override suspend fun clearUploadStatus() {
        dao.clearUploadStatus()
    }
    
}