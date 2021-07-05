package com.tokopedia.kyc_centralized.data.source

import com.tokopedia.kyc_centralized.data.model.response.KycData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class KycUploadImagesDataSource @Inject constructor(private val uploadImageDataSourceCloud: KycUploadImagesDataSourceCloud) {
    suspend fun uploadImages(projectId: RequestBody, params: RequestBody, ktpImage: MultipartBody.Part, faceImage: MultipartBody.Part): KycData {
        return uploadImageDataSourceCloud.uploadImage(projectId, params, ktpImage, faceImage)
    }
}