package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 15,May,2019
 */
data class TopAdsAutoAds (

        @SerializedName("data")
    val data: TopAdsAutoAdsData = TopAdsAutoAdsData(),
        @SerializedName("errors")
    val error: List<Error> = listOf()

) {
    data class Response(
            @SerializedName(value = "topAdsGetAutoAds", alternate = ["topAdsPostAutoAds"])
            val autoAds: TopAdsAutoAds
    )
}
