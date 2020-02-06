package com.tokopedia.topads.data.response

import com.google.gson.annotations.SerializedName

open class Group(

        var groupBudget: String = "1",
        var groupSchedule: String = "0",
        var source: String = "dashboard_add_product",
        var priceBid: Int = 0,
        var suggestedBidValue: Int = 0,
        var isCreateAds: Boolean = true,
        var previousBid: Int = 0,
        var stickerID: String = "3",
        var ads: List<AdsItem> = listOf(),
        var groupTotal: String = "1",
        var groupName: String = "",
        var priceDaily: Int = 0,
        var isSuggestedBidButton: String = "",
        var isCreateAff: Boolean = false
)