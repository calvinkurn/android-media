package com.tokopedia.buyerorderdetail.domain.models

import com.google.gson.annotations.SerializedName

data class GetBuyerOrderDetailParams(
        @SerializedName("cart_string")
        val cart: String = "",
        @SerializedName("order_id")
        val orderId: String = "0",
        @SerializedName("payment_id")
        val paymentId: String = "0"
)