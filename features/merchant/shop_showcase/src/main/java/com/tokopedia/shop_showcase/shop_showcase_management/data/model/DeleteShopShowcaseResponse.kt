package com.tokopedia.shop_showcase.shop_showcase_management.data.model


import com.google.gson.annotations.SerializedName

data class DeleteShopShowcaseResponse(
        @SerializedName("deleteShopShowcase")
        val deleteShopShowcase: DeleteShopShowcase = DeleteShopShowcase()
)

data class DeleteShopShowcase(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("success")
        val success: Boolean = false
)