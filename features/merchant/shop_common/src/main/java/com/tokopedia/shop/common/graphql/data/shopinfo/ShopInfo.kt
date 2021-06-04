package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.data.source.cloud.model.FreeOngkir

data class ShopInfo(
        @SerializedName("closedInfo")
        @Expose
        val closedInfo: ClosedInfo = ClosedInfo(),

        @SerializedName("createInfo")
        @Expose
        val createdInfo: CreatedInfo = CreatedInfo(),

        @SerializedName("favoriteData")
        @Expose
        val favoriteData: FavoriteData = FavoriteData(),

        @SerializedName("goldOS")
        @Expose
        val goldOS: GoldOS = GoldOS(),

        @SerializedName("isAllowManage")
        @Expose
        val isAllowManage: Int = 0,

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

        @SerializedName("statusInfo")
        @Expose
        val statusInfo: StatusInfo = StatusInfo(),

        @SerializedName("topContent")
        @Expose
        val topContent: TopContent = TopContent(),

        @SerializedName("bbInfo")
        @Expose
        val bbInfo: List<BBInfo> = listOf(),

        @SerializedName("freeOngkir")
        @Expose
        val freeOngkir: FreeOngkir = FreeOngkir(),

        @SerializedName("addressData")
        @Expose
        val addressData: AddressData = AddressData(),

        @SerializedName("shopHomeType")
        @Expose
        val shopHomeType: String = "",

        @SerializedName("os")
        @Expose
        val os: Os = Os(),

        @SerializedName("gold")
        @Expose
        val gold: Gold = Gold(),

        @SerializedName("activeProduct")
        @Expose
        val activeProduct: String = "",

        @SerializedName("shopStats")
        @Expose
        val shopStats: ShopStats = ShopStats(),

        @SerializedName("shopSnippetURL")
        @Expose
        val shopSnippetUrl: String = ""

) {
    fun isShopInfoNotEmpty():Boolean {
        return shopCore.shopID.isNotEmpty()
    }

    fun mapToShopInfoData(): ShopInfoData {
        val shipmentsData = shipments.map {
            it.mapToShipmentData()
        }

        return ShopInfoData(
                shopCore.shopID,
                shopCore.name,
                shopCore.description,
                shopCore.url,
                location,
                shopAssets.cover,
                shopCore.tagLine,
                goldOS.isOfficial,
                goldOS.isGold,
                createdInfo.openSince,
                shipmentsData,
                shopSnippetUrl
        )
    }

    companion object{
        @JvmField
        val TAG : String = ShopInfo::class.java.simpleName
    }

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
        val statusTitle: String = "",

        @SerializedName("isIdle")
        @Expose
        val isIdle: Boolean = false
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
    ) {
        //for tracking purpose
        val shopTypeString: String
            get() {
                return if (isOfficial == 1)
                    "official_store"
                else if (isGold == 1)
                    "gold_merchant"
                else
                    "reguler"
            }
    }

    var allowManage: Boolean = (isAllowManage == 1)

    data class ClosedInfo(
            @SerializedName("closedNote")
            @Expose
            val note: String = "",

            @SerializedName("reason")
            @Expose
            val reason: String = "",

            @SerializedName("until")
            @Expose
            val closeUntil: String = "",

            @SerializedName("detail")
            @Expose
            val closeDetail: CloseDetail = CloseDetail()
    )

    data class CloseDetail(
            @SerializedName("startDate")
            @Expose
            val startDate: String = "0",
            @SerializedName("endDate")
            @Expose
            val endDate: String = "0",
            @SerializedName("openDate")
            @Expose
            val openDateUnix: String = "",
            @SerializedName("status")
            @Expose
            val status: Int = 0,
    )

    data class CreatedInfo(
            @SerializedName("openSince")
            @Expose
            val openSince: String = "",

            @SerializedName("shopCreated")
            @Expose
            val shopCreated: String = ""
    )

    data class TopContent(
            @SerializedName("topURL")
            @Expose
            val topUrl: String = ""
    )

    data class AddressData(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("address")
            @Expose
            val address: String = "",

            @SerializedName("area")
            @Expose
            val area: String = "",

            @SerializedName("email")
            @Expose
            val email: String = "",

            @SerializedName("phone")
            @Expose
            val phone: String = "",

            @SerializedName("fax")
            @Expose
            val fax: String = ""
    )

    data class ShopStats(
            @SerializedName("productSold")
            @Expose
            val productSold: String = "",

            @SerializedName("totalTx")
            @Expose
            val totalTx: String = "",

            @SerializedName("totalShowcase")
            @Expose
            val totalShowcase: String = ""
    )
}