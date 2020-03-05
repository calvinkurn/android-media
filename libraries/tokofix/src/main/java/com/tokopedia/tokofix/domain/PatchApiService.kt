package com.tokopedia.tokofix.domain

import com.tokopedia.tokofix.Config
import com.tokopedia.tokofix.domain.data.DataResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Author errysuprayogi on 09,February,2020
 */
interface PatchApiService {

    @FormUrlEncoded
    @POST("patch")
    fun getPatch(@Field(value = "version") version: String): Call<DataResponse>

    @GET
    fun downloadPatch(@Url fileUrl: String): Call<ResponseBody>
}