package com.tokopedia.iris.data.network

import com.tokopedia.iris.util.MULTI_EVENT
import com.tokopedia.iris.util.SINGLE_EVENT
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by meta on 21/11/18.
 */
interface ApiInterface {

    @POST(SINGLE_EVENT)
    suspend fun sendSingleEventAsync(@Body data: RequestBody) : Response<String>

    @POST(MULTI_EVENT)
    suspend fun sendMultiEventAsync(@Body data: RequestBody) : Response<String>

}
