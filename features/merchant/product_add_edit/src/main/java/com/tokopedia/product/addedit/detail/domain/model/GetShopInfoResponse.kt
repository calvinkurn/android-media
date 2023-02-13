package com.tokopedia.product.addedit.detail.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetShopInfoResponse(
    @Expose
    @SerializedName("shopInfoByID")
    val shopInfoById: ShopInfoById
)

data class ShopInfoById(
    @SerializedName("result")
    val result: List<TokoShopInfoData>
)

data class TokoShopInfoData(
    @SerializedName("shopStats")
    val shopStats: StatsData,
    @SerializedName("goldOS")
    val goldOSData: GoldOSData
)

data class StatsData(
    @SerializedName("totalTxSuccess")
    val totalTxSuccess: String
)

data class GoldOSData(
    @SerializedName("shopTier")
    val shopTier: Int
)


