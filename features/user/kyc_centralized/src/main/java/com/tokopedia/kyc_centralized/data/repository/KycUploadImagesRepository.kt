package com.tokopedia.kyc_centralized.data.repository

import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.data.source.KycUploadImagesDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class KycUploadImagesRepository @Inject constructor(private val uploadImageDataSource: KycUploadImagesDataSource) {
    suspend fun uploadImages(projectId: RequestBody, params: RequestBody, ktpImage: MultipartBody.Part, faceImage: MultipartBody.Part): KycData {
        return uploadImageDataSource.uploadImages(projectId, params, ktpImage, faceImage)
    }
}