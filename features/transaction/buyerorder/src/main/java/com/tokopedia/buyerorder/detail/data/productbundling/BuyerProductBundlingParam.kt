package com.tokopedia.buyerorder.detail.data.productbundling

import com.google.gson.annotations.SerializedName

data class BuyerProductBundlingParam(
        @SerializedName("cart_string")
        val cart: String = "",
        @SerializedName("order_id")
        val orderId: String = "0",
        @SerializedName("payment_id")
        val paymentId: String = ""
)
