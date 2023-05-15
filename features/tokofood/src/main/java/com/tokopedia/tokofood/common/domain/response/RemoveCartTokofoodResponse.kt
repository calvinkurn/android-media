package com.tokopedia.tokofood.common.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO

data class RemoveCartTokofoodResponseNew(
    @SerializedName("cart_general_remove_cart")
    val cartGeneralRemoveCart: CartGeneralRemoveCart = CartGeneralRemoveCart()
) {
    fun isSuccess(): Boolean =
        cartGeneralRemoveCart.data.success == Int.ONE
}

data class CartGeneralRemoveCart(
    @SerializedName("data")
    val data: CartGeneralRemoveCartData = CartGeneralRemoveCartData()
)

data class CartGeneralRemoveCartData(
    @SerializedName("success")
    val success: Int = Int.ZERO,
    @SerializedName("message")
    val message: String = String.EMPTY
)
