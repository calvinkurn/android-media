package com.tokopedia.purchase_platform.features.promo.data.request

import com.google.gson.annotations.SerializedName

data class CouponListRequest(
        @SerializedName("codes")
        var codes: List<String> = emptyList(),
        @SerializedName("skip_apply")
        var skipApply: Int = 0,
        @SerializedName("is_suggested")
        var isSuggested: Int = 0,
        @SerializedName("cart_type")
        var cartType: String = "",
        @SerializedName("state")
        var state: String = "",
        @SerializedName("orders")
        var orders: List<Order> = emptyList()
)

data class Order(
        @SerializedName("shop_id")
        var shopId: Long = 0,
        @SerializedName("unique_id")
        var uniqueId: String = "",
        @SerializedName("product_details")
        var product_details: List<ProdustDetail> = emptyList(),
        @SerializedName("codes")
        var codes: List<String> = emptyList()
)

data class ProdustDetail(
        @SerializedName("product_id")
        var productId: Long,
        @SerializedName("quantity")
        var quantity: Int
)