package com.tokopedia.kyc_centralized.data.source

import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.data.network.KycUploadApi
import com.tokopedia.user_identification_common.KYCConstant.Companion.CO_BRAND_PROJECT_ID
import com.tokopedia.user_identification_common.KYCConstant.Companion.HOME_CREDIT_PROJECT_ID
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import javax.inject.Inject

class KycUploadImagesDataSourceCloud @Inject constructor(private val retrofit: Retrofit) {
    private var livenessData = KycData()

    suspend fun uploadImage(
            requestBodyProjectId: RequestBody,
            params: RequestBody,
            ktpImage: MultipartBody.Part,
            faceImage: MultipartBody.Part,
            projectId: String
    ): KycData {
        livenessData = when(projectId) {
            HOME_CREDIT_PROJECT_ID, CO_BRAND_PROJECT_ID -> uploadImagesAlaCarte(
                    requestBodyProjectId, params, ktpImage, faceImage)
            else -> uploadImagesKyc(requestBodyProjectId, params, ktpImage, faceImage)
        }
        return livenessData
    }

    private suspend fun uploadImagesKyc(
            projectId: RequestBody,
            params: RequestBody,
            ktpImage: MultipartBody.Part,
            faceImage: MultipartBody.Part
    ): KycData {
        return retrofit.create(KycUploadApi::class.java)
                .uploadImages(projectId, params, ktpImage, faceImage).data
    }

    private suspend fun uploadImagesAlaCarte(
            projectId: RequestBody,
            params: RequestBody,
            ktpImage: MultipartBody.Part,
            faceImage: MultipartBody.Part
    ): KycData {
        return retrofit.create(KycUploadApi::class.java)
                .uploadImagesAlaCarte(projectId, params, ktpImage, faceImage).data
    }
}