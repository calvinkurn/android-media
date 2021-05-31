package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class CartDetail(
        @SerializedName("cart_id")
        val cartId: String = "",
        @SerializedName("checkbox_state")
        val checkboxState: Boolean = false,
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("messages")
        val messages: List<String> = emptyList(),
        @SerializedName("product")
        val product: Product = Product(),
        @SerializedName("selected_unavailable_action_link")
        val selectedUnavailableActionLink: String = ""
)