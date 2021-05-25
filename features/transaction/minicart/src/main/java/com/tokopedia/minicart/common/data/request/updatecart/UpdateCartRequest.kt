package com.tokopedia.minicart.common.data.request.updatecart

import com.google.gson.annotations.SerializedName

data class UpdateCartRequest(
        @SerializedName("cart_id")
        var cartId: String = "",
        @SerializedName("quantity")
        var quantity: Int = 0,
        @SerializedName("notes")
        var notes: String = ""
)
