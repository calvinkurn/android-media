package com.tokopedia.entertainment.pdp.network_api

import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutBody
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutResponse
import com.tokopedia.entertainment.pdp.data.checkout.EventVerifyBody
import com.tokopedia.entertainment.pdp.data.checkout.EventVerifyResponse

interface EventCheckoutRepository {
    suspend fun postVerify(book:Map<String,Boolean>,eventVerifyBody: EventVerifyBody): EventVerifyResponse
    suspend fun postCheckout(book:Map<String,Boolean>,eventCheckoutBody: EventCheckoutBody):EventCheckoutResponse

}