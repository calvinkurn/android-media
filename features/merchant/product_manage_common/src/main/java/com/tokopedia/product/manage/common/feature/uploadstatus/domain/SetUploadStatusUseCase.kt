package com.tokopedia.product.manage.common.feature.uploadstatus.domain

import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.repository.UploadStatusRepository
import com.tokopedia.product.manage.common.feature.uploadstatus.data.model.UploadStatusModel
import com.tokopedia.product.manage.common.feature.uploadstatus.util.UploadStatusMapper.convertToEntity
import javax.inject.Inject

class SetUploadStatusUseCase @Inject constructor(
    private val repository: UploadStatusRepository
) {
    suspend fun setUploadStatus(uploadStatusModel: UploadStatusModel) {
        repository.setUploadStatus(uploadStatusModel.convertToEntity())
    }
}
