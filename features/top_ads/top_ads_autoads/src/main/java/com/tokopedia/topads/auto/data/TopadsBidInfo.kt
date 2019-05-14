package com.tokopedia.topads.auto.data

import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 15,May,2019
 */
class TopadsBidInfo {

    @SerializedName("request_type")
    var requestType: String? = ""
    @SerializedName("data")
    var data: List<BidInfoData> = listOf(BidInfoData())

}
