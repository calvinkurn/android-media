package com.tokopedia.updateinactivephone.usecase

import com.tokopedia.updateinactivephone.data.repository.UploadImageRepository
import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel
import com.tokopedia.usecase.RequestParams

class GetUploadHostUseCase(private val uploadImageRepository: UploadImageRepository) {

    suspend fun getUploadHost(requestParams: RequestParams): UploadHostModel {
        return uploadImageRepository.getUploadHost(requestParams.parameters)
    }

    companion object {
        const val PARAM_NEW_ADD = "new_add"
        const val DEFAULT_NEW_ADD = "2"
    }
}