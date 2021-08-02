package com.tokopedia.promocheckout.common.domain.deals

import com.google.gson.JsonObject
import com.tokopedia.promocheckout.common.domain.model.deals.DealsVerifyResponse
import com.tokopedia.url.TokopediaUrl
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

interface DealsCheckoutApi {

    @POST(DEALS_POST_VERIFY_PATH)
    fun postVerify(@QueryMap book: Map<String, Boolean>, @Body dealsVerifyBody: JsonObject): Observable<DealsVerifyResponse>

    companion object {
        /** move to util promo_checkout*/
        const val DEALS_POST_VERIFY_PATH = "v1/api/expresscart/verify"
        val BASE_URL_EVENT_DEALS = TokopediaUrl.getInstance().OMSCART
    }
}