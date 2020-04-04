package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

data class UpdateCartOccCartRequest(
        @SerializedName("cart_id")
        val cartId: String = "",
        @SerializedName("quantity")
        val quantity: Int = 1,
        @SerializedName("notes")
        val notes: String = "",
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("shipping_id")
        val shippingId: Int = 0,
        @SerializedName("sp_id")
        val spId: Int = 0
)