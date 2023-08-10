package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class GroupShopCart(
    @SerializedName("cart_string_order")
    val cartStringOrder: String = "",
    @SerializedName("shop")
    val shop: Shop = Shop(),
    @SerializedName("cart_details")
    val cartDetails: List<CartDetail> = emptyList()
)
