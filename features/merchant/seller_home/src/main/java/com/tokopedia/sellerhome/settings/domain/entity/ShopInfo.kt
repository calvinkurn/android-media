package com.tokopedia.sellerhome.settings.domain.entity

import com.google.gson.annotations.SerializedName

data class ShopInfo(
        @SerializedName("shopInfoMoengage")
        var shopInfoMoengage: ShopInfoMoengage? = ShopInfoMoengage(),
        @SerializedName("balance")
        var balance: Balance? = Balance()
)

data class ShopInfoMoengage (
        @SerializedName("info")
        var info: Info? = Info()
)

data class Info (
        @SerializedName("shop_name")
        var shopName: String? = "",
        @SerializedName("shop_avatar")
        var shopAvatar: String? = ""
)

data class Balance (
        @SerializedName("seller_usable_fmt")
        var sellerBalance: String = "") {
}