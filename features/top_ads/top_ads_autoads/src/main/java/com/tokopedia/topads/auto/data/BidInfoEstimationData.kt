package com.tokopedia.topads.auto.data

import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 15,May,2019
 */
class BidInfoEstimationData {

    @SerializedName("min_bid")
    var minBid: Int = 0
    @SerializedName("min_bid_fmt")
    var minBidFmt: String = ""
    @SerializedName("max_bid")
    var maxBid: Int = 0
    @SerializedName("max_bid_fmt")
    var maxBidFmt: String = ""
}
