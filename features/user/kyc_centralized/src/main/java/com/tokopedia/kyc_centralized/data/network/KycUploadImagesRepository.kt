package com.tokopedia.kyc_centralized.data.network

import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.data.model.KycResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class KycUploadImagesRepository @Inject constructor(
    private val api: KycUploadApi,
) {
    suspend fun uploadImages(
        requestBodyProjectId: RequestBody,
        params: RequestBody,
        ktpImage: MultipartBody.Part,
        faceImage: MultipartBody.Part,
        projectId: String
    ): KycResponse {
        return when (projectId) {
            KYCConstant.HOME_CREDIT_PROJECT_ID,
            KYCConstant.CO_BRAND_PROJECT_ID,
            KYCConstant.OFFICIAL_STORE_PROJECT_ID,
            KYCConstant.GO_CICIL_PROJECT_ID -> {
                uploadImagesAlaCarte(requestBodyProjectId, params, ktpImage, faceImage)
            }
            else -> uploadImagesKyc(requestBodyProjectId, params, ktpImage, faceImage)
        }
    }

    private suspend fun uploadImagesKyc(
        projectId: RequestBody,
        params: RequestBody,
        ktpImage: MultipartBody.Part,
        faceImage: MultipartBody.Part
    ): KycResponse {
        return api.uploadImages(projectId, params, ktpImage, faceImage)
    }

    private suspend fun uploadImagesAlaCarte(
        projectId: RequestBody,
        params: RequestBody,
        ktpImage: MultipartBody.Part,
        faceImage: MultipartBody.Part
    ): KycResponse {
        return api.uploadImagesAlaCarte(projectId, params, ktpImage, faceImage)
    }
}
