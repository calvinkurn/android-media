package com.tokopedia.common.network.data.source.cloud.api

import kotlinx.coroutines.experimental.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface RestApiDeferred {
    @GET
    fun getDeferred(@Url url: String, @QueryMap(encoded = true) queries: Map<String, Any?>, @HeaderMap headers: Map<String, Any?>): Deferred<Response<String>>

    @POST
    fun postDeferred(@Url url: String, @Body json: String, @QueryMap(encoded = true) queries: Map<String, Any?>, @HeaderMap headers: Map<String, Any?>): Deferred<Response<String>>

    @Multipart
    @POST
    fun postMultipartDeferred(@Url url: String, @Part file: MultipartBody.Part, @QueryMap(encoded = true) queries: Map<String, Any?>, @HeaderMap headers: Map<String, Any?>): Deferred<Response<String>>

    @Multipart
    @POST
    fun postMultipartDeferred(@Url url: String, @PartMap partMap: Map<String, RequestBody>, @QueryMap(encoded = true) queries: Map<String, Any?>, @HeaderMap headers: Map<String, Any?>): Deferred<Response<String>>

    @FormUrlEncoded
    @POST
    fun postDeferred(@Url url: String, @FieldMap(encoded = true) params: Map<String, Any?>, @QueryMap(encoded = true) queries: Map<String, Any?>, @HeaderMap headers: Map<String, Any?>): Deferred<Response<String>>

    @PUT
    fun putDeferred(@Url url: String, @Body json: String, @QueryMap(encoded = true) queries: Map<String, Any?>, @HeaderMap headers: Map<String, Any?>): Deferred<Response<String>>

    @FormUrlEncoded
    @PUT
    fun putDeferred(@Url url: String, @FieldMap(encoded = true) params: Map<String, Any?>, @QueryMap(encoded = true) queries: Map<String, Any?>, @HeaderMap headers: Map<String, Any?>): Deferred<Response<String>>

    @DELETE
    fun deleteDeferred(@Url url: String, @QueryMap(encoded = true) queries: Map<String, Any?>, @HeaderMap headers: Map<String, Any?>): Deferred<Response<String>>

    @Multipart
    @PUT
    fun putMultipartDeferred(@Url url: String, @Part file: MultipartBody.Part, @QueryMap(encoded = true) queries: Map<String, Any?>, @HeaderMap headers: Map<String, Any?>): Deferred<Response<String>>

    @Multipart
    @PUT
    fun putMultipartDeferred(@Url url: String, @PartMap partMap: Map<String, RequestBody>, @QueryMap(encoded = true) queries: Map<String, Any?>, @HeaderMap headers: Map<String, Any?>): Deferred<Response<String>>


    @PUT
    fun putRequestBodyDeferred(@Url url: String, @Body requestBody: RequestBody, @HeaderMap headers: Map<String, Any?>): Deferred<Response<String>>

}
