package com.tokopedia.minicart.common.promo.data.request

import com.google.gson.annotations.SerializedName

data class ValidateUseMvcParam(
    @SerializedName("codes")
    var codes: List<String> = emptyList(),
    @SerializedName("promo_ids")
    var promoIds: List<String> = emptyList(),
    @SerializedName("promo_type")
    var promoType: String = "",
    @SerializedName("state")
    var state: String = "",
    @SerializedName("cart_type")
    var cartType: String = "",
    @SerializedName("apply")
    var apply: Boolean = false,
    @SerializedName("orders")
    var orders: List<ValidateUseMvcOrderParam> = emptyList()
)

data class ValidateUseMvcOrderParam(
    @SerializedName("unique_id")
    var uniqueId: String = "",
    @SerializedName("shop_id")
    var shopId: String = "",
    @SerializedName("product_details")
    var productDetails: List<ValidateUseMvcProductParam> = emptyList()
)

data class ValidateUseMvcProductParam(
    @SerializedName("product_id")
    var productId: String = "",
    @SerializedName("quantity")
    var quantity: Int = 0
)
