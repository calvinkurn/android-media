package com.tokopedia.product.detail.data.model.shop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopInfo(
        @SerializedName("favoriteData")
        @Expose
        val favoriteData: FavoriteData = FavoriteData(),

        @SerializedName("goldOS")
        @Expose
        val goldOS: GoldOS = GoldOS(),

        @SerializedName("isAllowManage")
        @Expose
        val isAllowManage: Int = 0,

        @SerializedName("isOwner")
        @Expose
        val isOwner: Int = 0,

        @SerializedName("location")
        @Expose
        val location: String = "",

        @SerializedName("shipmentInfo")
        @Expose
        val shipments: List<ShopShipment> = listOf(),

        @SerializedName("shopAssets")
        @Expose
        val shopAssets: ShopAssets = ShopAssets(),

        @SerializedName("shopCore")
        @Expose
        val shopCore: ShopCore = ShopCore(),

        @SerializedName("shopLastActive")
        @Expose
        val shopLastActive: String = "",

        @SerializedName("shopTerms")
        @Expose
        val shopTerms: Int = 0,

        @SerializedName("statusInfo")
        @Expose
        val statusInfo: StatusInfo = StatusInfo()
){
    data class Response(
            @SerializedName("shopInfoByID")
            val result: Result = Result()
    )

    data class Result(
            @SerializedName("result")
            @Expose
            val data: List<ShopInfo> = listOf()
    )

    data class ShopAssets(
            @SerializedName("avatar")
            @Expose
            val avatar: String = "",

            @SerializedName("cover")
            @Expose
            val cover: String = ""
    )

    data class StatusInfo(
            @SerializedName("shopStatus")
            @Expose
            val shopStatus: Int = 0,

            @SerializedName("statusMessage")
            @Expose
            val statusMessage: String = "",

            @SerializedName("statusTitle")
            @Expose
            val statusTitle: String = ""
    )

    data class FavoriteData(
            @SerializedName("alreadyFavorited")
            @Expose
            val alreadyFavorited: Int = 0,

            @SerializedName("totalFavorite")
            @Expose
            val totalFavorite: Int = 0
    )

    data class GoldOS(
            @SerializedName("isGold")
            @Expose
            val isGold: Int = 0,

            @SerializedName("isGoldBadge")
            @Expose
            val isGoldBadge: Int = 0,

            @SerializedName("isOfficial")
            @Expose
            val isOfficial: Int = 0
    )

    var allowManage: Boolean = (isAllowManage == 1)
}