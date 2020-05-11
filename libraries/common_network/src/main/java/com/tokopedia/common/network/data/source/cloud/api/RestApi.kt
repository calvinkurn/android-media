package com.tokopedia.common.network.data.source.cloud.api

import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface RestApi {

    @GET
    operator fun get(@Url url: String, @QueryMap(encoded = true) queries: Map<String, String>, @HeaderMap headers: Map<String, String>): Observable<Response<String>>

    @POST
    fun post(@Url url: String, @Body json: String, @QueryMap(encoded = true) queries: Map<String, String>, @HeaderMap headers: Map<String, String>): Observable<Response<String>>

    @PATCH
    fun patch(@Url url: String, @Body json: String, @QueryMap(encoded = true) queries: Map<String, String>, @HeaderMap headers: Map<String, String>): Observable<Response<String>>

    @Multipart
    @POST
    fun postMultipart(@Url url: String, @Part file: MultipartBody.Part, @QueryMap(encoded = true) queries: Map<String, String>, @HeaderMap headers: Map<String, String>): Observable<Response<String>>

    @Multipart
    @POST
    fun postMultipart(@Url url: String, @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>, @QueryMap(encoded = true) queries: Map<String, String>, @HeaderMap headers: Map<String, String>): Observable<Response<String>>

    @FormUrlEncoded
    @POST
    fun post(@Url url: String, @FieldMap(encoded = true) params: Map<String, String>, @QueryMap(encoded = true) queries: Map<String, String>, @HeaderMap headers: Map<String, String>): Observable<Response<String>>

    @PUT
    fun put(@Url url: String, @Body json: String, @QueryMap(encoded = true) queries: Map<String, String>, @HeaderMap headers: Map<String, String>): Observable<Response<String>>

    @FormUrlEncoded
    @PUT
    fun put(@Url url: String, @FieldMap(encoded = true) params: Map<String, String>, @QueryMap(encoded = true) queries: Map<String, String>, @HeaderMap headers: Map<String, String>): Observable<Response<String>>

    @FormUrlEncoded
    @PATCH
    fun patch(@Url url: String, @FieldMap(encoded = true) params: Map<String, String>, @QueryMap(encoded = true) queries: Map<String, String>, @HeaderMap headers: Map<String, String>): Observable<Response<String>>

    @DELETE
    fun delete(@Url url: String, @QueryMap(encoded = true) queries: Map<String, String>, @HeaderMap headers: Map<String, String>): Observable<Response<String>>

    @Multipart
    @PUT
    fun putMultipart(@Url url: String, @Part file: MultipartBody.Part, @QueryMap(encoded = true) queries: Map<String, String>, @HeaderMap headers: Map<String, String>): Observable<Response<String>>

    @Multipart
    @PUT
    fun putMultipart(@Url url: String, @PartMap partMap: Map<String, RequestBody>, @QueryMap(encoded = true) queries: Map<String, String>, @HeaderMap headers: Map<String, String>): Observable<Response<String>>


    @PUT
    fun putRequestBody(@Url url: String, @Body requestBody: RequestBody, @HeaderMap headers: Map<String, String>): Observable<Response<String>>

    //Deferred
    @GET
    fun getDeferred(@Url url: String, @QueryMap(encoded = true) queries: Map<String, String>?, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>

    @POST
    fun postDeferred(@Url url: String, @Body json: String, @QueryMap(encoded = true) queries: Map<String, String>?, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>

    @Multipart
    @POST
    fun postMultipartDeferred(@Url url: String, @Part file: MultipartBody.Part, @QueryMap(encoded = true) queries: Map<String, String>?, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>

    @Multipart
    @POST
    fun postMultipartDeferred(@Url url: String, @PartMap partMap: Map<String, RequestBody>, @QueryMap(encoded = true) queries: Map<String, String>?, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>

    @FormUrlEncoded
    @POST
    fun postDeferred(@Url url: String, @FieldMap(encoded = true) params: Map<String, String>, @QueryMap(encoded = true) queries: Map<String, String>?, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>

    @PATCH
    fun patchDeferred(@Url url: String, @Body json: String, @QueryMap(encoded = true) queries: Map<String, String>?, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>

    @FormUrlEncoded
    @PATCH
    fun patchDeferred(@Url url: String, @FieldMap(encoded = true) params: Map<String, String>, @QueryMap(encoded = true) queries: Map<String, String>?, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>

    @PUT
    fun putDeferred(@Url url: String, @Body json: String, @QueryMap(encoded = true) queries: Map<String, String>?, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>

    @FormUrlEncoded
    @PUT
    fun putDeferred(@Url url: String, @FieldMap(encoded = true) params: Map<String, String>, @QueryMap(encoded = true) queries: Map<String, String>?, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>

    @DELETE
    fun deleteDeferred(@Url url: String, @QueryMap(encoded = true) queries: Map<String, String>?, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>

    @Multipart
    @PUT
    fun putMultipartDeferred(@Url url: String, @Part file: MultipartBody.Part, @QueryMap(encoded = true) queries: Map<String, String>?, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>

    @Multipart
    @PUT
    fun putMultipartDeferred(@Url url: String, @PartMap partMap: Map<String, RequestBody>, @QueryMap(encoded = true) queries: Map<String, String>?, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>


    @PUT
    fun putRequestBodyDeferred(@Url url: String, @Body requestBody: RequestBody, @HeaderMap headers: Map<String, String>?): Deferred<Response<String>>
}
