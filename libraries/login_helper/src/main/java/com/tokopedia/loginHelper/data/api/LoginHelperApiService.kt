package com.tokopedia.loginHelper.data.api

import com.tokopedia.loginHelper.data.response.LoginDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginHelperApiService {

    @GET("/users")
    suspend fun getUserData(@Query(value = "env") envType: String): Response<LoginDataResponse>
}
