package com.tokopedia.analyticsdebugger.debugger.data.network

import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author okasurya on 2019-08-16.
 */
interface TetraApi {
    @POST("api/debug/init")
    fun init(@Body data: RequestBody) : Deferred<Response<String>>

    @POST("api/debug/data")
    fun send(@Body data: RequestBody) : Deferred<Response<String>>
}