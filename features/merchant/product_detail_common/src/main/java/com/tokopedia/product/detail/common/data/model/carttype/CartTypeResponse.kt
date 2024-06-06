package com.tokopedia.product.detail.common.data.model.carttype

import android.content.Context
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.kotlin.extensions.view.ifNullOrEmpty
import com.tokopedia.product.detail.common.ProductCartHelper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.R

data class CartRedirection(
    @SerializedName("data")
    @Expose
    val data: List<CartTypeData> = listOf(),
    @SerializedName("alternate_copy")
    @Expose
    val alternateCopy: List<AlternateCopy> = listOf(),
    @SerializedName("error_message")
    @Expose
    val errorMessage: List<Any> = listOf(),
    @SerializedName("status")
    @Expose
    val status: String = ""
)

// Static wording from backend
data class AlternateCopy(
    @SerializedName("cart_type")
    @Expose
    val cartType: String = "",
    @SerializedName("color")
    @Expose
    val color: String = "",
    @SerializedName("text")
    @Expose
    val text: String = ""
)

data class CartTypeData(
    @SerializedName("product_id")
    @Expose
    val productId: String = "",
    @SerializedName("available_buttons")
    @Expose
    var availableButtons: List<AvailableButton> = listOf(),
    @SerializedName("unavailable_buttons")
    @Expose
    val unavailableButtons: List<String> = listOf(),
    @SerializedName("hide_floating_button")
    @Expose
    val hideFloatingButton: Boolean = false,
    @SerializedName("override_buttons")
    @Expose
    var overrideButtons: List<AvailableButton> = listOf(),
    @SerializedName("postATCLayout")
    @Expose
    val postAtcLayout: PostAtcLayout = PostAtcLayout()
) {

    // especially PDP, VBS as is
    val availableButtonsPriority: List<AvailableButton>
        get() = overrideButtons.ifNullOrEmpty { availableButtons }

    val shouldHideFloatingButtonInPdp
        get() = hideFloatingButton && overrideButtons.isEmpty()
}

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
    @SerializedName("showAnimation")
    @Expose
    val showAnimation: Boolean = false,
    @SerializedName("onboarding_message")
    @Expose
    val onboardingMessage: String = ""
) {
    fun isCartTypeDisabledOrRemindMe(): Boolean {
        return cartType == ProductDetailCommonConstant.KEY_DEFAULT_OOS || cartType == ProductDetailCommonConstant.KEY_REMIND_ME || cartType == ProductDetailCommonConstant.KEY_CHECK_WISHLIST
    }

    val atcKey: Int
        get() = ProductCartHelper.generateButtonAction(
            cartType = cartType,
            atcButton = showRecommendation
        )

    companion object {
        val AvailableButton?.buttonText: String
            get() = this?.text.ifNullOrBlank {
                // fallback to default text
                if (this?.cartType == ProductDetailCommonConstant.KEY_CHECK_WISHLIST) "Cek Wishlist" else "+Keranjang"
            }

        val AvailableButton?.orEmpty
            get() = this ?: AvailableButton(
                cartType = "",
                color = "",
                text = "",
                showRecommendation = false,
                showAnimation = false
            )

        fun createAddToCartButton(context: Context): AvailableButton = AvailableButton(
            cartType = ProductDetailCommonConstant.KEY_NORMAL_BUTTON,
            color = ProductDetailCommonConstant.KEY_BUTTON_PRIMARY_GREEN,
            text = context.getString(R.string.plus_product_to_cart),
            showRecommendation = true
        )

        fun createBuyNowButton(
            context: Context,
            isPreOrderActive: Boolean = false,
            isExpressCheckout: Boolean = true
        ): AvailableButton = AvailableButton(
            cartType = ProductDetailCommonConstant.KEY_NORMAL_BUTTON,
            color = ProductDetailCommonConstant.KEY_BUTTON_SECONDARY_GREEN,
            text = if (isPreOrderActive) {
                context.getString(R.string.action_preorder)
            } else {
                if (isExpressCheckout) {
                    context.getString(R.string.buy_now)
                } else {
                    context.getString(R.string.buy)
                }
            },
            showRecommendation = false
        )

        fun createOCSButton(): AvailableButton = AvailableButton(
            cartType = ProductDetailCommonConstant.KEY_OCS_BUTTON
        )
    }
}

data class PostAtcLayout(
    @SerializedName("layoutID")
    @Expose
    val layoutId: String = "",
    @SerializedName("postATCSession")
    @Expose
    val postAtcSession: String = "",
    @SerializedName("showPostATC")
    @Expose
    val showPostAtc: Boolean = false
)
