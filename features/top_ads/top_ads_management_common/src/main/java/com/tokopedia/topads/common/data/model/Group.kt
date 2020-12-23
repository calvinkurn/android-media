package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.SerializedName

open class Group(
        @SerializedName("groupBudget")
        var groupBudget: String = "1",
        @SerializedName("source")
        var source: String = "dashboard_add_product",
        @SerializedName("priceBid")
        var priceBid: Int = 0,
        @SerializedName("suggestedBidValue")
        var suggestedBidValue: Int = 0,
        @SerializedName("ads")
        var ads: List<AdsItem> = listOf(),
        @SerializedName("groupName")
        var groupName: String = "",
        @SerializedName("priceDaily")
        var priceDaily: Int = 0,
)