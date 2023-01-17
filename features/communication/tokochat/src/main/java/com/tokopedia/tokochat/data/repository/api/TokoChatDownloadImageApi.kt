package com.tokopedia.tokochat.data.repository.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface TokoChatDownloadImageApi {
    @GET
    suspend fun getImage(@Url url: String): ResponseBody
}
