package com.tokopedia.kyc_centralized.data.source

import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.data.network.KycUploadApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import javax.inject.Inject

class KycUploadImagesDataSourceCloud @Inject constructor(
    private val retrofit: Retrofit) {
    private var livenessData = KycData()

    suspend fun uploadImage(projectId: RequestBody, params: RequestBody, ktpImage: MultipartBody.Part, faceImage: MultipartBody.Part): KycData {
        livenessData =  retrofit.create(KycUploadApi::class.java).uploadImages(projectId, params, ktpImage, faceImage).data
        return livenessData
    }
}