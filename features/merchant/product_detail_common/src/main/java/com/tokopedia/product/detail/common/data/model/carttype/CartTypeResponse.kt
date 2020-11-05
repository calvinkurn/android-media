package com.tokopedia.product.detail.common.data.model.carttype


import com.google.gson.annotations.SerializedName

data class CartTypeData(
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("available_buttons")
        val availableButtons: MutableList<AvailableButton> = mutableListOf(),
        @SerializedName("config_name")
        val configName: String = "",
        @SerializedName("unavailable_buttons")
        val unavailableButtons: List<String> = listOf(),
        @SerializedName("hide_floating_button")
        val hideFloatingButton: Boolean = false
)

data class CartRedirection(
        @SerializedName("data")
        val data: List<CartTypeData> = listOf(),
        @SerializedName("error_message")
        val errorMessage: List<Any> = listOf(),
        @SerializedName("status")
        val status: String = ""
) {

    fun getCartTypeAtPosition(position: Int): CartTypeData? {
        return data.getOrNull(position)
    }
}

data class AvailableButton(
        @SerializedName("cart_type")
        val cartType: String = "",
        @SerializedName("color")
        val color: String = "",
        @SerializedName("text")
        val text: String = "",
        @SerializedName("show_recommendation")
        val showRecommendation: Boolean = false,
        @SerializedName("onboarding_message")
        val onboardingMessage: String = ""
)
