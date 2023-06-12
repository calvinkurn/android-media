package com.tokopedia.loginHelper.data.api

import com.tokopedia.loginHelper.data.body.LoginHelperAddUserBody
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.data.response.LoginHelperAddUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginHelperApiService {

    @GET("/users")
    suspend fun getUserData(@Query(value = "env") envType: String): Response<LoginDataResponse>

    @POST("/addUser")
    suspend fun addUser(@Body addUserBody: LoginHelperAddUserBody, @Query(value = "env") envType: String) : Response<LoginHelperAddUserResponse>
}
