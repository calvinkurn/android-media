package com.tokopedia.loginHelper.data.api

import com.tokopedia.loginHelper.data.body.LoginHelperAddUserBody
import com.tokopedia.loginHelper.data.response.LoginDataResponse
import com.tokopedia.loginHelper.data.response.LoginHelperAddUserResponse
import com.tokopedia.loginHelper.data.response.LoginHelperDeleteUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface LoginHelperApiService {

    @GET("/users")
    suspend fun getUserData(@Query(value = "env") envType: String): Response<LoginDataResponse>

    @POST("/addUser")
    suspend fun addUser(
        @Body addUserBody: LoginHelperAddUserBody,
        @Query(value = "env") envType: String
    ): Response<LoginHelperAddUserResponse>

    @PUT("/editUser")
    suspend fun editUser(
        @Body editUserBody: LoginHelperAddUserBody,
        @Query(value = "env") envType: String,
        @Query(value = "id") id: Long
    ): Response<LoginHelperAddUserResponse>

    @DELETE("/deleteUser")
    suspend fun deleteUser(
        @Query(value = "env") envType: String,
        @Query(value = "id") id: Long
    ): Response<LoginHelperDeleteUserResponse>
}
