package com.tokopedia.tokopatch.domain.api

import com.tokopedia.tokopatch.domain.data.TokenResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface OAuthApiService {

    @POST("token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    suspend fun token(@Body body: RequestBody): Response<TokenResponse>

}