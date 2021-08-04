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

/**Deprecated*/
interface EventCheckoutApi {

    @POST(EVENT_POST_VERIFY_PATH)
    fun postVerify(@QueryMap book: Map<String, Boolean>, @Body eventVerifyBody: EventVerifyBody): Observable<EventVerifyResponse>

    companion object{
        /** move to util promo_checkout*/
        const val EVENT_POST_VERIFY_PATH = "v1/api/expresscart/verify"
        val BASE_URL_EVENT = TokopediaUrl.getInstance().OMSCART
    }
}