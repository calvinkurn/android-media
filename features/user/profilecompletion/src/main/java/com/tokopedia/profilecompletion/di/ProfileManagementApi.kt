package com.tokopedia.profilecompletion.di

import com.tokopedia.profilecompletion.data.SecretResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileManagementApi {

    @GET("/goto-auth/secret")
    suspend fun getSecret(
        @Query("x-module_name") moduleName : String
    ): Response<SecretResponse>

}
