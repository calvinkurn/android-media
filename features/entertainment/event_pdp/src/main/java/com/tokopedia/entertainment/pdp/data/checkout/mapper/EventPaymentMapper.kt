package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutResponse
import com.tokopedia.entertainment.pdp.data.checkout.EventPaymentEntity

object EventPaymentMapper {
    fun getJsonMapper(data : EventCheckoutResponse):JsonObject{
        val gson = Gson()
        data.data.apply {
             if(data.data.amount>0) {
                return JsonParser().parse(gson.toJson(data.data)).asJsonObject
            }else{
                val eventPayment = EventPaymentEntity(
                        transactionId = transactionId
                )
                return JsonParser().parse(gson.toJson(eventPayment)).asJsonObject
            }
        }
    }
}