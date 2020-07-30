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
        @SerializedName("date_shop_created")
        val dateShopCreated: String = "",
        @Expose
        @SerializedName("shop_avatar")
        val shopAvatar: String = "",
        @Expose
        @SerializedName("shop_cover")
        val shopCover: String = "",
        @Expose
        @SerializedName("shop_domain")
        val shopDomain: String = "",
        @Expose
        @SerializedName("shop_id")
        val shopId: String = "",
        @Expose
        @SerializedName("shop_location")
        val shopLocation: String = "",
        @Expose
        @SerializedName("shop_name")
        val shopName: String = "",
        @Expose
        @SerializedName("shop_score")
        val shopScore: Int = 0,
        @Expose
        @SerializedName("total_active_product")
        val totalActiveProduct: Int = 0
)