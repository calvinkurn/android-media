package com.tokopedia.tokofood.common.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

data class UpdateQuantityTokofoodParam(
    @SerializedName("source")
    val source: String = String.EMPTY,
    @SerializedName("business_data")
    val businessData: UpdateQuantityTokofoodBusinessData = UpdateQuantityTokofoodBusinessData()
) {

    companion object {
        const val UPDATE_ACTION = "UPDATE"
    }

}

data class UpdateQuantityTokofoodBusinessData(
    @SerializedName("business_id")
    val businessId: String = String.EMPTY,
    @SerializedName("carts")
    val carts: List<UpdateQuantityTokofoodCart> = listOf()
)

data class UpdateQuantityTokofoodCart(
    @SerializedName("cart_id")
    val cartId: String = String.EMPTY,
    @SerializedName("quantity")
    val quantity: Int = Int.ZERO,
    @SerializedName("action")
    val action: String = UpdateQuantityTokofoodParam.UPDATE_ACTION
)
