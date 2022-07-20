package com.tokopedia.product.manage.common.feature.uploadstatus.domain

import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.repository.UploadStatusRepository
import javax.inject.Inject

class ClearUploadStatusUseCase @Inject constructor(
    private val repository: UploadStatusRepository
) {
    suspend fun clearUploadStatus() {
        repository.clearUploadStatus()
    }
}