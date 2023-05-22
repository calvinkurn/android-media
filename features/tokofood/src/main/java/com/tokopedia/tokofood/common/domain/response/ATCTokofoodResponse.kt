package com.tokopedia.tokofood.common.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil

data class ATCTokofoodResponse(
    @SerializedName("cart_general_add_to_cart")
    val cartGeneralAddToCart: CartGeneralAddToCart = CartGeneralAddToCart()
) {
    fun isSuccess(): Boolean = cartGeneralAddToCart.data.success == TokoFoodCartUtil.SUCCESS_STATUS_INT
}

data class CartGeneralAddToCart(
    @SerializedName("data")
    val data: CartGeneralAddToCartData = CartGeneralAddToCartData()
)

data class CartGeneralAddToCartData(
    @SerializedName("success")
    val success: Int = Int.ZERO,
    @SerializedName("message")
    val message: String = String.EMPTY,
    @SerializedName("data")
    val data: CartGeneralAddToCartDataData = CartGeneralAddToCartDataData()
)

data class CartGeneralAddToCartDataData(
    @SerializedName("business_data")
    val businessData: List<CartListBusinessData> = listOf()
) {

    fun getIsShowBottomSheet(): Boolean {
        return getTokofoodBusinessData().customResponse.bottomSheet.isShowBottomSheet
    }

    fun getTokofoodBusinessData(): CartListBusinessData {
        return businessData.firstOrNull { it.businessId == TokoFoodCartUtil.getBusinessId() }
            ?: CartListBusinessData()
    }

}
