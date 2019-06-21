package com.tokopedia.topads.auto.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 15,May,2019
 */
data class BidInfoData (

    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("suggestion_bid")
    val suggestionBid: Int = 0,
    @SerializedName("suggestion_bid_fmt")
    val suggestionBidFmt: String = "",
    @SerializedName("min_bid")
    val minBid: Int = 0,
    @SerializedName("min_bid_fmt")
    val minBidFmt: String = "",
    @SerializedName("max_bid")
    val maxBid: Int = 0,
    @SerializedName("max_bid_fmt")
    val maxBidFmt: String = "",
    @SerializedName("multiplier")
    val multiplier: Int = 0,
    @SerializedName("min_daily_budget")
    val minDailyBudget: Int = 0,
    @SerializedName("min_daily_budget_fmt")
    val minDailyBudgetFmt: String = "",
    @SerializedName("max_daily_budget")
    val maxDailyBudget: Int = 0,
    @SerializedName("max_daily_budget_fmt")
    val maxDailyBudgetFmt: String = "",
    @SerializedName("estimation")
    val estimation: BidInfoEstimationData = BidInfoEstimationData(),
    @Expose(serialize = false, deserialize = false)
    var shopStatus: Int = 0

)
