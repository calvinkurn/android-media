package ai.advance.liveness.data.network

import ai.advance.liveness.data.model.response.LivenessResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface LivenessDetectionApi {
    @Multipart
    @POST("kycapp/api/v1/validate-register")
    suspend fun uploadImages(@Part("project_id") projectId: RequestBody,
                             @Part("params") params: RequestBody,
                             @Part ktpImage: MultipartBody.Part,
                             @Part faceImage: MultipartBody.Part
    ): LivenessResponse
}