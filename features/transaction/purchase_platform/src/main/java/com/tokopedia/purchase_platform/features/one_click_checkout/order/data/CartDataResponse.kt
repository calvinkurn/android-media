package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

data class CartDataResponse(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("cart_id")
        val cartId: Int = 0,
        @SerializedName("product")
        val product: ProductDataResponse = ProductDataResponse()
)