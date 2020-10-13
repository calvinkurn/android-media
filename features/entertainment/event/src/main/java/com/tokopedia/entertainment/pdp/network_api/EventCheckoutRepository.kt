package com.tokopedia.entertainment.pdp.network_api

import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutResponse
import com.tokopedia.entertainment.pdp.data.redeem.validate.EventValidateResponse
import com.tokopedia.entertainment.pdp.data.redeem.validate.EventValidateUser
import com.tokopedia.promocheckout.common.domain.model.event.Cart
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse

interface EventCheckoutRepository {
    suspend fun postVerify(book:Map<String,Boolean>,eventVerifyBody: EventVerifyBody): EventVerifyResponse?
    suspend fun postCheckout(cart: Cart?):EventCheckoutResponse?
    suspend fun validateRedeem(eventValidateUser: EventValidateUser):EventValidateResponse?
}