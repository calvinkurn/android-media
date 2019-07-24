package com.tokopedia.topads.auto.data.network.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.auto.data.entity.TopAdsShopInfoData

/**
 * Author errysuprayogi on 15,May,2019
 */
data class TopAdsShopInfo(
        @SerializedName("data")
        val data: TopAdsShopInfoData = TopAdsShopInfoData(),
        @SerializedName("errors")
        val error: List<ErrorResponse> = listOf(ErrorResponse())
) {
    data class Response(
            @SerializedName("topAdsGetShopInfo")
            val shopInfo: TopAdsShopInfo
    )
}

