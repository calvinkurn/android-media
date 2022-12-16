package com.tokopedia.tokochat.data.repository.api

import com.tokopedia.tokochat.domain.response.extension.TokoChatImageResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface TokoChatImageApi {

    @GET("v1/image/{id}/url")
    suspend fun getImageUrl(
        @Path("id") id: String,
        @Header("Image-Key") channelId: String
    ): TokoChatImageResult
}
