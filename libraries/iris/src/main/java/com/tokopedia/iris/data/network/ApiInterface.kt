package com.tokopedia.iris.data.network

import com.tokopedia.iris.MULTI_EVENT
import com.tokopedia.iris.SINGLE_EVENT
import kotlinx.coroutines.experimental.Deferred
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by meta on 21/11/18.
 */
interface ApiInterface {

    @POST(SINGLE_EVENT)
    fun sendSingleEvent(@Body data: RequestBody) : Deferred<Response<String>>

    @POST(MULTI_EVENT)
    fun sendMultiEvent(@Body data: RequestBody) : Deferred<Response<String>>

}
