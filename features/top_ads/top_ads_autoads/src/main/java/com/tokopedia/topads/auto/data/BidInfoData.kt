package com.tokopedia.topads.auto.data

import com.google.gson.annotations.SerializedName

/**
 * Author errysuprayogi on 15,May,2019
 */
class BidInfoData {

    @SerializedName("id")
    var id: Int = 0
    @SerializedName("suggestion_bid")
    var suggestionBid: Int = 0
    @SerializedName("suggestion_bid_fmt")
    var suggestionBidFmt: String = ""
    @SerializedName("min_bid")
    var minBid: Int = 0
    @SerializedName("min_bid_fmt")
    var minBidFmt: String = ""
    @SerializedName("max_bid")
    var maxBid: Int = 0
    @SerializedName("max_bid_fmt")
    var maxBidFmt: String = ""
    @SerializedName("multiplier")
    var multiplier: Int = 0
    @SerializedName("min_daily_budget")
    var minDailyBudget: Int = 0
    @SerializedName("min_daily_budget_fmt")
    var minDailyBudgetFmt: String = ""
    @SerializedName("max_daily_budget")
    var maxDailyBudget: Int = 0
    @SerializedName("max_daily_budget_fmt")
    var maxDailyBudgetFmt: String = ""
    @SerializedName("estimation")
    var estimation: BidInfoEstimationData = BidInfoEstimationData()

}
