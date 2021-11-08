package com.tokopedia.promocheckout.common.domain.deals

import com.google.gson.JsonObject
import com.tokopedia.promocheckout.common.domain.model.deals.DealsVerifyResponse
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

interface DealsCheckoutApi {

    @POST("v1/api/expresscart/verify")
    fun postVerify(@QueryMap book: Map<String, Boolean>, @Body dealsVerifyBody: JsonObject): Observable<DealsVerifyResponse>

    companion object {

        val BASE_URL_EVENT_DEALS = if (TokopediaUrl.getInstance().TYPE == Env.LIVE) "https://omscart.tokopedia.com/"
        else "https://omscart-staging.tokopedia.com/"
    }
}