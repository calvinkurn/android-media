package com.tokopedia.topads.auto.data.network.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.auto.data.entity.TopAdsShopInfoData

/**
 * Author errysuprayogi on 15,May,2019
 */
data class TopAdsShopInfoResponse (

    @SerializedName("data")
    val data: TopAdsShopInfoData = TopAdsShopInfoData()

)
