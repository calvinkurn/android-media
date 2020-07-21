package com.tokopedia.product.detail.common.data.model.carttype


import com.google.gson.annotations.SerializedName

data class CartRedirectionResponse(
        @SerializedName("cart_redirection")
        val cartRedirection: CartRedirection? = null
)

data class CartTypeData(
        @SerializedName("availableButtons")
        val availableButtons: List<AvailableButton> = listOf(),
        @SerializedName("configName")
        val configName: String = "",
        @SerializedName("unavailableButtons")
        val unavailableButtons: List<String> = listOf()
)

data class CartRedirection(
        @SerializedName("cartRedirectionData")
        val data: List<CartTypeData> = listOf(),
        @SerializedName("errorMessage")
        val errorMessage: List<Any> = listOf(),
        @SerializedName("status")
        val status: String = ""
) {

    fun getCartTypeAtPosition(position: Int): CartTypeData? {
        return data.getOrNull(position)
    }
}

data class AvailableButton(
        @SerializedName("cartType")
        val cartType: String = "",
        @SerializedName("color")
        val color: String = "",
        @SerializedName("text")
        val text: String = "",
        @SerializedName("showRecommendation")
        val showRecommendation: Boolean = false,
        @SerializedName("onBoardingMessage")
        val onboardingMessage: String = ""
)
