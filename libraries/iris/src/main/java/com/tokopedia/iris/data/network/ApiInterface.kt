package com.tokopedia.iris.data.network

import com.tokopedia.iris.*
import kotlinx.coroutines.experimental.Deferred
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by meta on 21/11/18.
 */
interface ApiInterface {

    @POST(SINGLE_EVENT)
    fun sendSingleEvent(
            @Header(HEADER_CONTENT_TYPE) contentType: String,
            @Header(HEADER_DEVICE) device: String,
            @Header(HEADER_USER_ID) userId: String,
            @Body data: RequestBody) : Deferred<Response<String>>

    @POST(MULTI_EVENT)
    fun sendMultiEvent(
            @Header(HEADER_CONTENT_TYPE) contentType: String,
            @Header(HEADER_DEVICE) device: String,
            @Header(HEADER_USER_ID) userId: String,
            @Body data: RequestBody) : Deferred<Response<String>>

}
