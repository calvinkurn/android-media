package com.tokopedia.purchase_platform.features.promo.data.request

import com.google.gson.annotations.SerializedName

data class CouponListRequest(
        @SerializedName("codes")
        var codes: List<String> = emptyList(),
        @SerializedName("skip_apply")
        var skipApply: Int = 1,
        @SerializedName("is_suggested")
        var isSuggested: Int = 1,
        @SerializedName("cart_type")
        var cartType: String = "default", // ocs & default
        @SerializedName("state")
        var state: String = "", // cart & checkout & occ
        @SerializedName("orders")
        var orders: List<Order> = emptyList()
)

data class Order(
        @SerializedName("shop_id")
        var shopId: Long = 0,
        @SerializedName("unique_id")
        var uniqueId: String = "",
        @SerializedName("product_details")
        var product_details: List<ProductDetail> = emptyList(),
        @SerializedName("codes")
        var codes: List<String> = emptyList(),
        @SerializedName("is_checked")
        var isChecked: Boolean = false
)

data class ProductDetail(
        @SerializedName("product_id")
        var productId: Long = 0,
        @SerializedName("quantity")
        var quantity: Int = -1
)