package com.tokopedia.topads.auto.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 15,May,2019
 */
data class BidInfoEstimationData (

    @SerializedName("min_bid")
    val minBid: Int = 0,
    @SerializedName("min_bid_fmt")
    val minBidFmt: String = "",
    @SerializedName("max_bid")
    val maxBid: Int = 0,
    @SerializedName("max_bid_fmt")
    val maxBidFmt: String = ""
)
