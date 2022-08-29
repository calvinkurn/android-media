package com.tokopedia.kyc_centralized.data.repository

import com.tokopedia.kyc_centralized.data.model.response.KycResponse
import com.tokopedia.kyc_centralized.data.source.KycUploadImagesDataSourceCloud
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class KycUploadImagesRepository @Inject constructor(
        private val uploadImageDataSource: KycUploadImagesDataSourceCloud
) {
    suspend fun uploadImages(
            requestBodyProjectId: RequestBody,
            params: RequestBody,
            ktpImage: MultipartBody.Part,
            faceImage: MultipartBody.Part,
            projectId: String
    ): KycResponse {
        return uploadImageDataSource.uploadImage(
                requestBodyProjectId, params, ktpImage, faceImage, projectId)
    }
}