package com.tokopedia.profilecompletion.di

import com.tokopedia.profilecompletion.data.SeamlessResponse
import com.tokopedia.profilecompletion.data.SecretResponse
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProfileManagementApi {

    @GET("/goto-auth/secret")
    suspend fun getSecret(
        @Query("x-module_name") moduleName : String
    ): Response<SecretResponse>

    @FormUrlEncoded
    @POST("/goto-auth/seamless")
    suspend fun postSeamless(
        @FieldMap params: Map<String, String>
    ): Response<SeamlessResponse>

}
