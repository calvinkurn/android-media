package com.tokopedia.sellerhome.settings.domain.entity

import com.google.gson.annotations.SerializedName

data class ShopInfo(
        @SerializedName("shopInfoMoengage")
        var shopInfoMoengage: ShopInfoMoengage? = ShopInfoMoengage(),
        @SerializedName("balance")
        var balance: Balance? = Balance(),
        @SerializedName("topadsDeposit")
        val topadsDeposit: TopadsDeposit = TopadsDeposit()
)

data class ShopInfoMoengage (
        @SerializedName("info")
        var info: Info? = Info(),
        @SerializedName("owner")
        var owner: Owner? = Owner()
)

data class Info (
        @SerializedName("shop_name")
        var shopName: String? = "",
        @SerializedName("shop_avatar")
        var shopAvatar: String? = ""
)

data class Owner (
        @SerializedName("pm_status")
        var pmStatus: String? = "",
        @SerializedName("is_gold_merchant")
        var isGoldMerchant: Boolean? = false,
        @SerializedName("is_seller")
        var isSeller: Boolean? = false
)

data class Balance (
        @SerializedName("seller_usable")
        var sellerBalance: Long = 0) {
}

data class TopadsDeposit(
        @SerializedName("topads_amount")
        val topadsAmount : Int = 0,
        @SerializedName("is_topads_user")
        val isTopadsUser : Boolean = false
)