package com.tokopedia.iris.data.network

import com.tokopedia.iris.MULTI_EVENT
import com.tokopedia.iris.SINGLE_EVENT
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 * Created by meta on 21/11/18.
 */
interface ApiInterface {

    @POST(SINGLE_EVENT)
    fun sendSingleEvent(@Body() data: RequestBody) : Observable<Response<String>>

    @POST(MULTI_EVENT)
    fun sendMultiEvent(@Body() data: RequestBody) : Observable<Response<String>>

}
