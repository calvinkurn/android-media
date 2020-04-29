package com.tokopedia.entertainment.pdp.data.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EventCheckoutBody(
        @SerializedName("cart_items")
        @Expose
        val cartItems: List<CartItem> = emptyList()
)