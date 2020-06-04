package com.tokopedia.shop_showcase.shop_showcase_management.data.model

import com.google.gson.annotations.SerializedName

data class ReorderShopShowcaseResponse(
        @SerializedName("reorderShopShowcase")
        val reorderShopShowcase: ReorderShopShowcase = ReorderShopShowcase()
)

data class ReorderShopShowcase(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("success")
        val success: Boolean = false
)