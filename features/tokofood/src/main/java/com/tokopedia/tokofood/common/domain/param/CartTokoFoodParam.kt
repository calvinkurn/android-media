package com.tokopedia.tokofood.common.domain.param

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil

data class ATCCartTokoFoodParam(
    @SerializedName("additional_attributes")
    val additionalAttributes: String = "",
    @SerializedName("carts")
    val carts: List<ATCCartItemTokoFoodParam> = listOf()
)

data class RemoveCartTokoFoodParamOld(
    @SerializedName("carts")
    val carts: List<RemoveItemTokoFoodParam> = listOf()
) {
    companion object {

        @JvmStatic
        fun getProductParamById(
            productId: String,
            cartId: String,
            shopId: String
        ): RemoveCartTokoFoodParamOld {
            val cartList = listOf(
                RemoveItemTokoFoodParam(
                    cartId = cartId.toLongOrZero(),
                    productId = productId,
                    shopId = shopId
                )
            )
            return RemoveCartTokoFoodParamOld(carts = cartList)
        }

    }
}

data class CartTokoFoodParam(
    @SerializedName("additional_attributes")
    val additionalAttributes: String = "",
    @SerializedName("carts")
    val carts: List<CartItemTokoFoodParam> = listOf()
)

data class ATCCartItemTokoFoodParam(
    @SuppressLint("Invalid Data Type")
    @SerializedName("business_id")
    val businessId: Long = TokoFoodCartUtil.TOKOFOOD_BUSINESS_ID,
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("shop_id")
    val shopId: String = "",
    @SerializedName("quantity")
    val quantity: Int = 0,
    @SerializedName("metadata")
    val metadata: String = ""
)

data class RemoveItemTokoFoodParam(
    @SuppressLint("Invalid Data Type")
    @SerializedName("business_id")
    val businessId: Long = TokoFoodCartUtil.TOKOFOOD_BUSINESS_ID,
    @SuppressLint("Invalid Data Type")
    @SerializedName("cart_id")
    val cartId: Long = 0,
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("shop_id")
    val shopId: String = ""
)

data class CartItemTokoFoodParam(
    @SuppressLint("Invalid Data Type")
    @SerializedName("business_id")
    val businessId: Long = TokoFoodCartUtil.TOKOFOOD_BUSINESS_ID,
    @SuppressLint("Invalid Data Type")
    @SerializedName("cart_id")
    val cartId: Long = 0,
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("shop_id")
    val shopId: String = "",
    @SerializedName("quantity")
    val quantity: Int = 0,
    @SerializedName("metadata")
    val metadata: String = ""
)
