package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import com.tokopedia.cartrevamp.view.uimodel.CartOnBoardingBottomSheetData

data class OnboardingBottomSheet(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("button_text")
    val buttonText: String = "",
    @SerializedName("image_url")
    val imageUrl: String = ""
) {
    fun shouldShowOnBoardingBottomSheet(): Boolean {
        return type.isNotBlank() && (title.isNotBlank() || description.isNotBlank())
    }

    fun getBottomSheetOnBoardingData(): CartOnBoardingBottomSheetData {
        return CartOnBoardingBottomSheetData(
            type = type,
            title = title,
            description = description,
            buttonText = buttonText,
            imageUrl = imageUrl
        )
    }

    companion object {
        private const val MULTIPLE_BO_BOTTOM_SHEET_TYPE = "cart_page_multiple_bo"
    }
}
