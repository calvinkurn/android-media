package com.tokopedia.tokochat.data.repository.api

import com.tokopedia.tokochat.domain.response.upload_image.TokoChatUploadImageResult
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface TokoChatUploadImageApi {
    @Multipart
    @POST("v1/image/url")
    suspend fun uploadImage(
        @Part("file") file: RequestBody,
        @Part("key") key: RequestBody
    ): TokoChatUploadImageResult
}
