package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateCartOccCartRequest(
        @SerializedName("cart_id")
        @Expose
        val cartId: String = "",
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 1,
        @SerializedName("notes")
        @Expose
        val notes: String = "",
        @SerializedName("product_id")
        @Expose
        val productId: String = "",
        @SerializedName("shipping_id")
        @Expose
        val shippingId: Int = 0,
        @SerializedName("sp_id")
        @Expose
        val spId: Int = 0
)