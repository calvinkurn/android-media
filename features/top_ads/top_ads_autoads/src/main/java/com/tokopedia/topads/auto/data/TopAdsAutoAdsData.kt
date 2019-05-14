package com.tokopedia.topads.auto.data

import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 15,May,2019
 */
class TopAdsAutoAdsData {

    @SerializedName("shop_id")
    var shopId: Int = 0
    @SerializedName("status")
    var status: Int = 0
    @SerializedName("status_desc")
    var statusDesc: String? = null
    @SerializedName("daily_budget")
    var dailyBudget: Int = 0
    @SerializedName("daily_usage")
    var dailyUsage: Int = 0
    @SerializedName("info")
    var adsInfo: TopAdsAutoAdsInfo = TopAdsAutoAdsInfo()
}
