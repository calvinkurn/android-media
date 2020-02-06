package com.tokopedia.topads.data

import com.tokopedia.topads.data.response.AdsItem
import com.tokopedia.topads.data.response.ResponseCreateGroup

open class CreateAdGroupModel  {
    var groupType: String = ""

    var groupSchedule: String = ""

    var source: String = ""

    var previousBid: Int = 0

    var groupTotal: String = ""

    var priceDaily: Int = 0

    var groupEndTime: String = ""

    var groupStartTime: String = ""

    var isCreateAff: Boolean = false

    var isAutoAds: Boolean = false

    var isSuggestionBidButton: String = ""

    var statusDesc: String = ""

    var groupBudget: String = ""

    var isSuggestionBidvarue: String = ""

    var isEnoughDeposit: Boolean = false

    var groupID: String = ""

    var departmentID: String = ""

    var oldPriceBid: Int = 0

    var toggle: String = ""

    var priceBid: Int = 0

    var suggestedBidvarue: Int = 0

    var isCreateAds: Boolean = false

    var stickerID: String = ""

    var ads: List<CreateAdItemModel> = listOf()

    var groupName: String = ""

    var groupStartDate: String = ""

    var groupEndDate: String = ""

    var keywordTotal: String = ""

    var shopID: String = ""

    var status: String = ""

}