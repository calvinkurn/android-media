package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.orZero
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
        val shopSnippetUrl: String = "",

    @SerializedName("badgeURL")
        @Expose
        val shopTierBadgeUrl: String = "",

    @SerializedName("shopTier")
        @Expose
        val shopTier: Int = 0,

    @SerializedName("branchLinkDomain")
        @Expose
        val branchLinkDomain: String = "",

    @SerializedName("tickerData")
        @Expose
        val tickerData: List<TickerDataResponse> = emptyList(),

    @SerializedName("isGoApotik")
        @Expose
        val isGoApotik: Boolean = false,

    @SerializedName("epharmacyInfo")
        @Expose
        val epharmacyInfo: EPharmacyInfo = EPharmacyInfo(),

    @SerializedName("partnerInfo")
        @Expose
        val partnerInfo: List<PartnerInfoData> = listOf(),

    @SerializedName("shopMultilocation")
        @Expose
        val shopMultilocation: ProductShopMultilocation = ProductShopMultilocation(),

    @SerializedName("partnerLabel")
        @Expose
        val partnerLabel: String = String.EMPTY
) {
    fun isShopInfoNotEmpty():Boolean {
        return shopCore.shopID.isNotEmpty()
    }

    fun mapToShopInfoData(): ShopInfoData {
        val shipmentsData = shipments.map {
            it.mapToShipmentData()
        }

        return ShopInfoData(
            shopId = shopCore.shopID,
            name = shopCore.name,
            description = shopCore.description,
            url = shopCore.url,
            location = location,
            imageCover = shopAssets.cover,
            tagLine = shopCore.tagLine,
            isOfficial = goldOS.isOfficial,
            isGold = goldOS.isGold,
            openSince = createdInfo.openSince,
            shipments = shipmentsData,
            shopSnippetUrl = shopSnippetUrl,
            isGoApotik = isGoApotik,
            siaNumber = epharmacyInfo.siaNumber,
            sipaNumber = epharmacyInfo.sipaNumber,
            apj = epharmacyInfo.apj,
            partnerLabel = partnerLabel,
            fsType = partnerInfo.firstOrNull()?.fsType.orZero(),
            partnerName = partnerInfo.firstOrNull()?.partnerName.orEmpty()
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

    data class EPharmacyInfo(
        @SerializedName("siaNumber")
        @Expose
        val siaNumber: String = "",

        @SerializedName("sipaNumber")
        @Expose
        val sipaNumber: String = "",

        @SerializedName("apj")
        @Expose
        val apj: String = "",
    )

    data class PartnerInfoData(
        @SerializedName("partnerName")
        @Expose
        val partnerName: String = "",

        @SerializedName("fsType")
        @Expose
        val fsType: Int = 0,
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
        val isIdle: Boolean = false,

        @SerializedName("tickerType")
        @Expose
        val tickerType: String = ""
    ) {
        companion object {
            private const val ON_MODERATED_STAGE = 3
            private const val ON_MODERATED_PERMANENTLY = 5
        }

        fun isOnModerationMode(): Boolean {
            val status = shopStatus
            return status == ON_MODERATED_STAGE || status == ON_MODERATED_PERMANENTLY
        }
    }


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
        val isOfficial: Int = 0,

        @SerializedName("badge")
        @Expose
        val badge: String = ""
    ) {

        companion object{
            private const val IS_OFFICIAL_STORE_VALUE = 1
            private const val IS_GOLD_MERCHANT_VALUE = 1
        }
        //for tracking purpose
        val shopTypeString: String
            get() {
                return if (isOfficial == IS_OFFICIAL_STORE_VALUE)
                    "official_store"
                else if (isGold == IS_GOLD_MERCHANT_VALUE)
                    "gold_merchant"
                else
                    "reguler"
            }
        fun isOfficialStore() = isOfficial == IS_OFFICIAL_STORE_VALUE
        fun isGoldMerchant() = isGold == IS_GOLD_MERCHANT_VALUE
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
            @SerializedName("openDateUTC")
            @Expose
            val openDateUnixUtc: String = "",
            @SerializedName("status")
            @Expose
            val status: Int = 0
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

    data class TickerDataResponse(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("color")
        @Expose
        val color: String = "",
        @SerializedName("link")
        @Expose
        val link: String = "",
        @SerializedName("action")
        @Expose
        val action: String = "",
        @SerializedName("actionLink")
        @Expose
        val actionLink: String = "",
        @SerializedName("tickerType")
        @Expose
        val tickerType: Int = 0,
        @SerializedName("actionBottomSheet")
        @Expose
        val actionBottomSheet: TickerActionBs = TickerActionBs()
    ) {
        data class TickerActionBs(
            @SerializedName("title")
            @Expose
            val title: String = "",
            @SerializedName("message")
            @Expose
            val message: String = "",
            @SerializedName("reason")
            @Expose
            val reason: String = "",
            @SerializedName("buttonText")
            @Expose
            val buttonText: String = "",
            @SerializedName("buttonLink")
            @Expose
            val buttonLink: String = ""
        )
    }

}
