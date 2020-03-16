package com.tokopedia.product.detail.common.data.model.carttype


import com.google.gson.annotations.SerializedName

data class CartRedirectionResponse(
        @SerializedName("cart_redirection")
        val cartRedirection: CartRedirection = CartRedirection()
)

data class CartTypeResponse(
        @SerializedName("data")
        val response: CartRedirectionResponse = CartRedirectionResponse()
)

data class CartTypeData(
        @SerializedName("available_buttons")
        val availableButtons: List<AvailableButton> = listOf(),
        @SerializedName("config_name")
        val configName: String = "",
        @SerializedName("show_recommendation")
        val showRecommendation: Boolean = false,
        @SerializedName("unavailable_buttons")
        val unavailableButtons: List<Any> = listOf()
)

data class CartRedirection(
        @SerializedName("data")
        val data: List<CartTypeData> = listOf(),
        @SerializedName("error_message")
        val errorMessage: List<Any> = listOf(),
        @SerializedName("status")
        val status: String = ""
)

data class AvailableButton(
        @SerializedName("cart_type")
        val cartType: String = "",
        @SerializedName("color")
        val color: String = "",
        @SerializedName("text")
        val text: String = ""
)
