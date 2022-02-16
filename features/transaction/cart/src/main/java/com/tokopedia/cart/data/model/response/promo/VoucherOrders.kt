package com.tokopedia.cart.data.model.response.promo

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 09/03/20.
 */
data class VoucherOrders (
        @SerializedName("code")
        var code: String = "",

        @SerializedName("unique_id")
        var uniqueId: String = "",

        @SerializedName("message")
        var message: MessageVoucherOrders = MessageVoucherOrders(),

        @SerializedName("cart_id")
        val cartId: String = ""
)