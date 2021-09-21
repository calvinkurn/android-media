package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.SerializedName

open class Group(
        @SerializedName("groupBudget")
        var groupBudget: String = "1",
        @SerializedName("source")
        var source: String = "dashboard_add_product",
        @SerializedName("priceBid")
        var priceBid: Double = 0.0,
        @SerializedName("suggestedBidValue")
        var suggestedBidValue: Double = 0.0,
        @SerializedName("ads")
        var ads: List<AdsItem> = listOf(),
        @SerializedName("groupName")
        var groupName: String = "",
        @SerializedName("priceDaily")
        var priceDaily: Double = 0.0,
        @SerializedName("strategies")
        var strategies: List<String>? = listOf()
)