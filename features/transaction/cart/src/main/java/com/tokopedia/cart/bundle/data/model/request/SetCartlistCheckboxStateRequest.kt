package com.tokopedia.cart.bundle.data.model.request

import com.google.gson.annotations.SerializedName

data class SetCartlistCheckboxStateRequest(
        @SerializedName("cart_id")
        var cartId: String = "",
        @SerializedName("checkbox_state")
        var checkboxState: Boolean = false
)