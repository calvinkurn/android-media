package com.tokopedia.entertainment.pdp.network_api

import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutBody
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutResponse
import com.tokopedia.entertainment.pdp.data.checkout.EventVerifyBody
import com.tokopedia.entertainment.pdp.data.checkout.EventVerifyResponse
import retrofit2.Response
import retrofit2.http.*

interface EventCheckoutApi {

    @POST(PATH_VERIFY)
    suspend fun postVerify(@QueryMap book:Map<String,Boolean>,  @Body eventVerifyBody: EventVerifyBody): Response<EventVerifyResponse>

    @POST(PATH_CHECKOUT)
    suspend fun postCheckout(@QueryMap book:Map<String,Boolean>,  @Body eventCheckoutBody: EventCheckoutBody): Response<EventCheckoutResponse>
}