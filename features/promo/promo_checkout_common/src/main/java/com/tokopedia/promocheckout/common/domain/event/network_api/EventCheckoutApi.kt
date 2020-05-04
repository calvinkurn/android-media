package com.tokopedia.promocheckout.common.domain.event.network_api

import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

interface EventCheckoutApi {

    @POST("v1/api/expresscart/verify")
    fun postVerify(@QueryMap book: Map<String, Boolean>, @Body eventVerifyBody: EventVerifyBody): Observable<EventVerifyResponse>

    companion object{
        const val BASE_URL_EVENT = "https://omscart-staging.tokopedia.com/"
    }
}