package com.tokopedia.tokofood.common.domain.param

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil

data class CartTokoFoodParam(
    // TODO: Create correct object for parsed chosenAddress
    @SerializedName("additional_attributes")
    @Expose
    val additionalAttributes: String = "",
    @SerializedName("carts")
    @Expose
    val carts: List<CartItemTokoFoodParam> = listOf()
)

data class CartItemTokoFoodParam(
    @SuppressLint("Invalid Data Type") 
    @SerializedName("business_id")
    @Expose
    val businessId: Long = TokoFoodCartUtil.TOKOFOOD_BUSINESS_ID,
    @SuppressLint("Invalid Data Type")
    @SerializedName("cart_id")
    @Expose
    val cartId: Long = 0,
    @SerializedName("product_id")
    @Expose
    val productId: String = "",
    @SerializedName("shop_id")
    @Expose
    val shopId: String = "",
    @SerializedName("quantity")
    @Expose
    val quantity: Int = 0,
    @SerializedName("metadata")
    @Expose
    val metadata: String = ""
)
