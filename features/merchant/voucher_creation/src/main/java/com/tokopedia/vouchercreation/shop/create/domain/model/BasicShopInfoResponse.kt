package com.tokopedia.vouchercreation.shop.create.domain.model

import com.google.gson.annotations.SerializedName

data class BasicShopInfoResponse(
        @SerializedName("shopInfoMoengage")
        var shopInfoMoengage: ShopInfoMoengage = ShopInfoMoengage()
)

data class ShopInfoMoengage (
        @SerializedName("info")
        var shopInfo: ShopInfo = ShopInfo()
)

data class ShopInfo (
        @SerializedName("shop_name")
        var shopName: String = "",
        @SerializedName("shop_avatar")
        var shopAvatar: String = ""
)