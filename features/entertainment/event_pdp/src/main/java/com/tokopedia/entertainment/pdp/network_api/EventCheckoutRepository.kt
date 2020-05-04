package com.tokopedia.entertainment.pdp.network_api

import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutBody
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutResponse
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse

interface EventCheckoutRepository {
    suspend fun postVerify(book:Map<String,Boolean>,eventVerifyBody: EventVerifyBody): EventVerifyResponse
    suspend fun postCheckout(book:Map<String,Boolean>,eventCheckoutBody: EventCheckoutBody):EventCheckoutResponse

}