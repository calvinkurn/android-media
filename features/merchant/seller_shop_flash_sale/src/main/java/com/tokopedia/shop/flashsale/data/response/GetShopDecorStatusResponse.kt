package com.tokopedia.shop.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class GetShopDecorStatusResponse(
    @SerializedName("shopPageGetHomeType")
    val shopPageGetHomeType: ShopPageGetHomeType = ShopPageGetHomeType()
) {
    data class ShopPageGetHomeType(
        @SerializedName("shopHomeType")
        val shopHomeType: String = ""
    )
}