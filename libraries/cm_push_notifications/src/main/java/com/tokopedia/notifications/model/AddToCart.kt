package com.tokopedia.notifications.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

data class AddToCart(
        @Expose @SerializedName(CMConstant.PayloadKeys.PRODUCT_ID) var productId: Int? = 0,
        @Expose @SerializedName(CMConstant.PayloadKeys.PRODUCT_NAME) var productName: String? = "",
        @Expose @SerializedName(CMConstant.PayloadKeys.PRODUCT_BRAND) var productBrand: String? = "",
        @Expose @SerializedName(CMConstant.PayloadKeys.PRODUCT_PRICE) var productPrice: Float? = 0f,
        @Expose @SerializedName(CMConstant.PayloadKeys.PRODUCT_VARIANT) var productVariant: String? = "",
        @Expose @SerializedName(CMConstant.PayloadKeys.PRODUCT_QUANTITY) var productQuantity: String? = "",
        @Expose @SerializedName(CMConstant.PayloadKeys.SHOP_ID) var shopId: Int? = 0,
        @Expose @SerializedName(CMConstant.PayloadKeys.SHOP_NAME) var shopName: String? = "",
        @Expose @SerializedName(CMConstant.PayloadKeys.SHOP_TYPE) var shopType: String? = ""
)