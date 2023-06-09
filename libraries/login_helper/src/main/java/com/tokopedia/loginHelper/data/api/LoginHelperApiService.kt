package com.tokopedia.loginHelper.data.api

import com.tokopedia.loginHelper.data.response.LoginDataResponse
import retrofit2.Response
import retrofit2.http.GET

interface LoginHelperApiService {

    @GET("/users?env=staging")
    suspend fun getUserData(): Response<LoginDataResponse>
}
