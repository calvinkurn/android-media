package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetShopInfoResponse(
        @Expose
        @SerializedName("shopInfoMoengage")
        val shopInfoMoengage: ShopInfoMoengage?
)

data class ShopInfoMoengage(
        @Expose
        @SerializedName("info")
        val info: Info?
)

data class Info(
        @Expose
        @SerializedName("shop_avatar")
        val shopAvatar: String = "",
        @Expose
        @SerializedName("shop_name")
        val shopName: String = ""
)