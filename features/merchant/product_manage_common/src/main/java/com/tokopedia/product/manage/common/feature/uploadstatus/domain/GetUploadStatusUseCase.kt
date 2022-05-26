package com.tokopedia.product.manage.common.feature.uploadstatus.domain

import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.repository.UploadStatusRepository
import com.tokopedia.product.manage.common.feature.uploadstatus.data.model.UploadStatusModel
import com.tokopedia.product.manage.common.feature.uploadstatus.util.UploadStatusMapper.convertToModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class GetUploadStatusUseCase @Inject constructor(
    private val repository: UploadStatusRepository
) {
    fun getUploadStatus(): Flow<UploadStatusModel> = repository.getUploadStatus().mapNotNull { uploadStatus ->
        uploadStatus?.convertToModel()
    }
}
