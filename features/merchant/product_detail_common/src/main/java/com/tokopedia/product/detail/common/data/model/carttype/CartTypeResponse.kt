package com.tokopedia.product.detail.common.data.model.carttype


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CartRedirectionResponse(
        @SerializedName("cartRedirection")
        @Expose
        val response: CartRedirection = CartRedirection()
)

data class CartRedirection(
        @SerializedName("data")
        @Expose
        val data: List<CartTypeData> = listOf(),
        @SerializedName("error_message")
        @Expose
        val errorMessage: List<Any> = listOf(),
        @SerializedName("status")
        @Expose
        val status: String = ""
)

data class CartTypeData(
        @SerializedName("product_id")
        @Expose
        val productId: String = "",
        @SerializedName("available_buttons")
        @Expose
        val availableButtons: List<AvailableButton> = listOf(),
        @SerializedName("config_name")
        @Expose
        val configName: String = "",
        @SerializedName("unavailable_buttons")
        @Expose
        val unavailableButtons: List<String> = listOf(),
        @SerializedName("hide_floating_button")
        @Expose
        val hideFloatingButton: Boolean = false
)

data class AvailableButton(
        @SerializedName("cart_type")
        @Expose
        val cartType: String = "",
        @SerializedName("color")
        @Expose
        val color: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("show_recommendation")
        @Expose
        val showRecommendation: Boolean = false,
        @SerializedName("onboarding_message")
        @Expose
        val onboardingMessage: String = ""
)
