package com.tokopedia.kyc_centralized.data.source

import com.tokopedia.kyc_centralized.data.model.response.KycResponse
import com.tokopedia.kyc_centralized.data.network.KycUploadApi
import com.tokopedia.user_identification_common.KYCConstant.Companion.CO_BRAND_PROJECT_ID
import com.tokopedia.user_identification_common.KYCConstant.Companion.GO_CICIL_PROJECT_ID
import com.tokopedia.user_identification_common.KYCConstant.Companion.HOME_CREDIT_PROJECT_ID
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class KycUploadImagesDataSourceCloud @Inject constructor(
    private val api: KycUploadApi
) {

    suspend fun uploadImage(
        requestBodyProjectId: RequestBody,
        params: RequestBody,
        ktpImage: MultipartBody.Part,
        faceImage: MultipartBody.Part,
        projectId: String
    ): KycResponse {
        return when(projectId) {
            HOME_CREDIT_PROJECT_ID,
            CO_BRAND_PROJECT_ID,
            GO_CICIL_PROJECT_ID -> {
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