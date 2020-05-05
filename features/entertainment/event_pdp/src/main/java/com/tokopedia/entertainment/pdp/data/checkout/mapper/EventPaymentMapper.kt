package com.tokopedia.entertainment.pdp.data.checkout.mapper

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutResponse
import com.tokopedia.entertainment.pdp.data.checkout.EventPaymentEntity

object EventPaymentMapper {
    fun getJsonMapper(data : EventCheckoutResponse):JsonObject{
        var eventPayment = EventPaymentEntity()

        data.data.apply {
            if(data.data.amount>0) {
                eventPayment = EventPaymentEntity(
                        amount = amount,
                        currency = currency,
                        customerEmail = customerEmail,
                        customerMsisdn = customerMsisdn,
                        customerName = customerName,
                        hitPG = hitPG,
                        itemsname = itemsname,
                        itemsprice = itemsprice,
                        itemsquantity = itemsquantity,
                        merchantCode = merchantCode,
                        message = message,
                        paymentMetadata = paymentMetadata,
                        profileCode = profileCode,
                        refundAmount = refundAmount,
                        signature = signature,
                        status = status,
                        transactionDate = transactionDate,
                        transactionId = transactionId,
                        userDefinedValue = userDefinedValue
                )
            }else{
                eventPayment = EventPaymentEntity(
                        transactionId = transactionId
                )
            }
        }

        val gson = Gson()
        return JsonParser().parse(gson.toJson(eventPayment)).asJsonObject
    }
}