package com.tokopedia.promocheckout.common.domain.event.network_api

import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

interface EventCheckoutApi {

    @POST("v1/api/expresscart/verify")
    fun postVerify(@QueryMap book: Map<String, Boolean>, @Body eventVerifyBody: EventVerifyBody): Observable<EventVerifyResponse>

    companion object{

        val BASE_URL_EVENT = if(TokopediaUrl.getInstance().TYPE == Env.LIVE) "https://omscart.tokopedia.com/"
                                    else "https://omscart-staging.tokopedia.com/"
    }
}