package com.tokopedia.topads.dashboard.recommendation.data.model.cloud


import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.dashboard.recommendation.data.model.local.TopAdsGetShopInfoUiModel

data class TopAdsGetShopInfoResponse(
    @SerializedName("topadsGetShopInfoV2_1")
    val topAdsGetShopInfoV21: TopAdsGetShopInfoV21,
) {
    data class TopAdsGetShopInfoV21(
        @SerializedName("data")
        val topAdsGetShopInfo: TopAdsGetShopInfoData,

        @SerializedName("errors")
        val errors: List<Error> = listOf()

    ) {
        data class TopAdsGetShopInfoData(
            @SerializedName("ads")
            val ads: List<Ad>
        ) {
            data class Ad(
                @SerializedName("is_used")
                val isUsed: Boolean,
                @SerializedName("type")
                val type: String
            )
        }
    }

    fun toTopAdsGetShopInfoModel(): TopAdsGetShopInfoUiModel {
        val topAdsGetShopInfoModel = TopAdsGetShopInfoUiModel()
        topAdsGetShopInfoV21.topAdsGetShopInfo.ads.forEach {
            if (it.type == "product" && it.isUsed) topAdsGetShopInfoModel.isProduct = true
            if (it.type == "headline" && it.isUsed) topAdsGetShopInfoModel.isHeadline = true
        }
        return topAdsGetShopInfoModel
    }
}
