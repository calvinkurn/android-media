package com.tokopedia.topads.auto.data.network.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.auto.data.entity.TopAdsAutoAdsData

/**
 * Author errysuprayogi on 15,May,2019
 */
data class TopAdsAutoAds (

    @SerializedName("data")
    val data: TopAdsAutoAdsData = TopAdsAutoAdsData(),
    @SerializedName("errors")
    val error: List<ErrorResponse> = listOf(ErrorResponse())

) {
    data class Response(
            @SerializedName(value = "topAdsGetAutoAds", alternate = ["topAdsPostAutoAds"])
            val autoAds: TopAdsAutoAds
    )
}
