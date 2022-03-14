package com.tokopedia.oneclickcheckout.order.data.gocicil

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.kotlin.extensions.view.toLongOrZero

class GoCicilInstallmentRequest(
        val gatewayCode: String = "",
        val merchantCode: String = "",
        val profileCode: String = "",
        val userId: String = "",
        val paymentAmount: Double = 0.0,
        val merchantType: String = "",
) {
    val userDefinedValue
        get() = JsonObject().apply {
            addProperty(USER_DEFINED_VALUE_KEY_USER_ID, userId.toLongOrZero())
        }.toString()

    val orderMetadata
        get() = JsonObject().apply {
            add(ORDER_METADATA_KEY_ORDER_DATA, JsonArray().apply {
                add(JsonObject().apply {
                    addProperty(ORDER_METADATA_KEY_MERCHANT_TYPE, merchantType)
                    addProperty(ORDER_METADATA_KEY_ORDER_AMOUNT, paymentAmount)
                })
            })
        }.toString()

    companion object {
        private const val USER_DEFINED_VALUE_KEY_USER_ID = "user_id"

        private const val ORDER_METADATA_KEY_ORDER_DATA = "order_data"
        private const val ORDER_METADATA_KEY_MERCHANT_TYPE = "merchant_type"
        private const val ORDER_METADATA_KEY_ORDER_AMOUNT = "order_amount"
    }
}