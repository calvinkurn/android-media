package com.tokopedia.minicart.common.promo.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateUseMvcParam(
    @Expose
    @SerializedName("codes")
    var codes: List<String> = emptyList(),
    @Expose
    @SerializedName("promo_ids")
    var promoIds: List<String> = emptyList(),
    @Expose
    @SerializedName("promo_type")
    var promoType: String = "",
    @Expose
    @SerializedName("state")
    var state: String = "",
    @Expose
    @SerializedName("cart_type")
    var cartType: String = "",
    @Expose
    @SerializedName("apply")
    var apply: Boolean = false,
    @Expose
    @SerializedName("orders")
    var orders: List<ValidateUseMvcOrderParam> = emptyList()
)

data class ValidateUseMvcOrderParam(
    @Expose
    @SerializedName("unique_id")
    var uniqueId: String = "",
    @Expose
    @SerializedName("shop_id")
    var shopId: String = "",
    @Expose
    @SerializedName("product_details")
    var productDetails: List<ValidateUseMvcProductParam> = emptyList()
)

data class ValidateUseMvcProductParam(
    @Expose
    @SerializedName("product_id")
    var productId: String = "",
    @Expose
    @SerializedName("quantity")
    var quantity: Int = 0
)