package com.tokopedia.tokopatch.domain.api

import com.tokopedia.tokopatch.domain.data.DataResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Author errysuprayogi on 09,February,2020
 */
interface PatchApiService {

    @FormUrlEncoded
    @POST("stability")
    fun getPatch(
        @Header(value = "Authorization") authorization: String,
        @Field(value = "package") packageName: String,
        @Field(value = "version") versionName: String,
        @Field(value = "build_number") buildNumber: String,
        @Field(value = "env") env: String
    ): Call<DataResponse>
}