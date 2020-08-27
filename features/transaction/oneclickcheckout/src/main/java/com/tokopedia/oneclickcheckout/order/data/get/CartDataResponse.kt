package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

data class CartDataResponse(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("cart_id")
        val cartId: Long = 0,
        @SerializedName("product")
        val product: ProductDataResponse = ProductDataResponse(),
        @SerializedName("shop")
        val shop: ShopDataResponse = ShopDataResponse(),
        @SerializedName("cart_string")
        val cartString: String = "",
        @SerializedName("payment_profile")
        val paymentProfile: String = ""
)