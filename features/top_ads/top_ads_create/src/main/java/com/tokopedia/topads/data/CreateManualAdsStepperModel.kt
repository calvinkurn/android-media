package com.tokopedia.topads.data

import com.tokopedia.abstraction.base.view.model.StepperModel
import com.tokopedia.topads.data.response.KeywordDataItem
import kotlinx.android.parcel.Parcelize

/**
 * Author errysuprayogi on 01,November,2019
 */
@Parcelize
open class CreateManualAdsStepperModel(
        var groupName: String = "",
        var selectedProductIds: MutableList<Int> = mutableListOf(),
        var selectedKeywords: MutableList<String> = mutableListOf(),
        var STAGE: Int = 0,
        var selectedKeywordStage: MutableList<KeywordDataItem> = mutableListOf(),
        var selectedSuggestBid: MutableList<Int> = mutableListOf(),
        var selectedKeywordType: MutableList<Int> = mutableListOf(),
        var minSuggestBidKeyword: Int = 0, // for keywords
        var suggestedBidPerClick: Int = 0, // for Default
        var finalBidPerClick: Int = -1, // Edited Bid by User
        var maxBid: Int = 0,
        var minBid: Int = 0,
        var dailyBudget: Int = 0,
        var adIds: MutableList<Int> = mutableListOf(),
        var adIdsPromo: MutableList<Int> = mutableListOf(),
        var adIdsNonPromo: MutableList<Int> = mutableListOf(),
        var selectedPromo: MutableList<Int> = mutableListOf(),
        var selectedNonPromo: MutableList<Int> = mutableListOf()) : StepperModel