package com.tokopedia.kyc_centralized.data.network

import com.tokopedia.kyc_centralized.data.model.response.KycResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface KycUploadApi {
    @Multipart
    @POST("kycapp/api/v1/validate-register")
    suspend fun uploadImages(@Part("project_id") projectId: RequestBody,
                             @Part("params") params: RequestBody,
                             @Part ktpImage: MultipartBody.Part,
                             @Part faceImage: MultipartBody.Part
    ): KycResponse
}