package com.tokopedia.entertainment.pdp.network_api

import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutBody
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutResponse
import com.tokopedia.promocheckout.common.domain.model.event.Cart
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse
import java.io.IOException
import javax.inject.Inject

class EventCheckoutRepositoryImpl @Inject constructor(private val eventCheckoutApi: EventCheckoutApi): EventCheckoutRepository{

    override suspend fun postVerify(book:Map<String,Boolean>,eventVerifyBody: EventVerifyBody): EventVerifyResponse? {
        val response = eventCheckoutApi.postVerify(book,eventVerifyBody)
        if (response.isSuccessful) {
            return response.body()
        }
        throw IOException(ERROR_DEFAULT)
    }

    override suspend fun postCheckout(cart: Cart?): EventCheckoutResponse? {
        cart.let {
            val response = eventCheckoutApi.postCheckout(it)
            if (response.isSuccessful) {
                return response.body()
            }
        }

        throw IOException(ERROR_DEFAULT)
    }

    companion object {
        const val ERROR_DEFAULT = "Terjadi Kesalahan, silakan ulang beberapa saat lagi"
    }
}