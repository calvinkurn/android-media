package com.tokopedia.entertainment.pdp.network_api

import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutBody
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutResponse
import com.tokopedia.promocheckout.common.domain.model.event.Cart
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import retrofit2.Response
import retrofit2.http.*

interface EventCheckoutApi {

    @POST(PATH_VERIFY)
    suspend fun postVerify(@QueryMap book:Map<String,Boolean>,  @Body eventVerifyBody: EventVerifyBody): Response<EventVerifyResponse>

    @POST(PATH_CHECKOUT)
    suspend fun postCheckout(@Body cart: Cart?): Response<EventCheckoutResponse>

    companion object{
        val BASE_URL = if(TokopediaUrl.getInstance().TYPE == Env.LIVE) "https://omscart.tokopedia.com/"
        else "https://omscart-staging.tokopedia.com/"
    }
}