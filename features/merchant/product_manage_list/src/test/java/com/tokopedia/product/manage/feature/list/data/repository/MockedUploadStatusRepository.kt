package com.tokopedia.product.manage.feature.list.data.repository

import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.entity.UploadStatusEntity
import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.repository.UploadStatusRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MockedUploadStatusRepository(private val uploadStatusEntity: UploadStatusEntity): UploadStatusRepository {

    var flowEntity = flow<UploadStatusEntity?> {
        emit(uploadStatusEntity)
    }

    override fun getUploadStatus(): Flow<UploadStatusEntity?> {
        return flowEntity
    }

    override suspend fun setUploadStatus(uploadStatusEntity: UploadStatusEntity) {
        // do nothing
    }

    override suspend fun clearUploadStatus() {
        flowEntity = flowEntity.map { null }
    }

}