package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.SerializedName

data class GetOccCartData(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("cart_list")
        val cartList:
)