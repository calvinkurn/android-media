package com.tokopedia.kyc_centralized.data.network

import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.data.model.KycResponse
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class KycUploadImagesRepository @Inject constructor(
    private val api: KycUploadApi,
    private val kycSharedPreference: KycSharedPreference
) {
    suspend fun uploadImages(
        requestBodyProjectId: RequestBody,
        params: RequestBody,
        ktpImage: MultipartBody.Part,
        faceImage: MultipartBody.Part,
        selfieMode: RequestBody
    ): KycResponse {
        return when (kycSharedPreference.getStringCache(KYCConstant.SharedPreference.KEY_KYC_FLOW_TYPE)) {
            KYCConstant.SharedPreference.VALUE_KYC_FLOW_TYPE_ALA_CARTE -> {
                uploadImagesAlaCarte(requestBodyProjectId, params, ktpImage, faceImage, selfieMode)
            }
            else -> uploadImagesKyc(requestBodyProjectId, params, ktpImage, faceImage, selfieMode)
        }
    }

    private suspend fun uploadImagesKyc(
        projectId: RequestBody,
        params: RequestBody,
        ktpImage: MultipartBody.Part,
        faceImage: MultipartBody.Part,
        selfieMode: RequestBody
    ): KycResponse {
        return api.uploadImages(projectId, params, ktpImage, faceImage, selfieMode)
    }

    private suspend fun uploadImagesAlaCarte(
        projectId: RequestBody,
        params: RequestBody,
        ktpImage: MultipartBody.Part,
        faceImage: MultipartBody.Part,
        selfieMode: RequestBody
    ): KycResponse {
        return api.uploadImagesAlaCarte(projectId, params, ktpImage, faceImage, selfieMode)
    }
}
