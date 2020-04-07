package com.tokopedia.topads.data.param

import com.google.gson.annotations.SerializedName

open class Group(
        @SerializedName("groupBudget")
        var groupBudget: String = "1",
        @SerializedName("groupSchedule")
        var groupSchedule: String = "0",
        @SerializedName("source")
        var source: String = "dashboard_add_product",
        @SerializedName("priceBid")
        var priceBid: Int = 0,
        @SerializedName("suggestedBidValue")
        var suggestedBidValue: Int = 0,
        @SerializedName("isCreateAds")
        var isCreateAds: Boolean = true,
        @SerializedName("previousBid")
        var previousBid: Int = 0,
        @SerializedName("stickerID")
        var stickerID: String = "3",
        @SerializedName("ads")
        var ads: List<AdsItem> = listOf(),
        @SerializedName("groupTotal")
        var groupTotal: String = "1",
        @SerializedName("groupName")
        var groupName: String = "",
        @SerializedName("priceDaily")
        var priceDaily: Int = 0,
        @SerializedName("isSuggestedBidButton")
        var isSuggestedBidButton: String = "",
        @SerializedName("isCreateAff")
        var isCreateAff: Boolean = false
)