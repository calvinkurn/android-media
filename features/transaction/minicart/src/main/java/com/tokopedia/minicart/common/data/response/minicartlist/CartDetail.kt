package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class CartDetail(
        @SerializedName("cart_id")
        val cartId: String = "",
        @SerializedName("selected_unavailable_action_link")
        val selectedUnavailableActionLink: String = "",
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("product")
        val product: Product = Product()
)