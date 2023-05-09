package com.tokopedia.tokochat.data.repository.api

import com.tokopedia.tokochat.domain.response.upload_image.TokoChatUploadImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface TokoChatUploadImageApi {
    @Multipart
    @POST("v1/image/url")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("key") key: RequestBody
    ): TokoChatUploadImageResponse
}
