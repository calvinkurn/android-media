package com.tokopedia.tokofood.common.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO

data class UpdateQuantityTokofoodResponse(
    @SerializedName("cart_general_update_cart_quantity")
    val cartGeneralUpdateCartQuantity: CartGeneralUpdateCartQuantity = CartGeneralUpdateCartQuantity()
) {

    fun isSuccess(): Boolean = cartGeneralUpdateCartQuantity.data.success == Int.ONE

}

data class CartGeneralUpdateCartQuantity(
    @SerializedName("data")
    val data: CartGeneralUpdateCartQuantityData = CartGeneralUpdateCartQuantityData()
)

data class CartGeneralUpdateCartQuantityData(
    @SerializedName("success")
    val success: Int = Int.ZERO,
    @SerializedName("message")
    val message: String = String.EMPTY
)
