package com.tokopedia.topads.data

import com.tokopedia.abstraction.base.view.model.StepperModel
import kotlinx.android.parcel.Parcelize

/**
 * Author errysuprayogi on 01,November,2019
 */
@Parcelize
open class CreateManualAdsStepperModel(
        var groupId: String = "",
        var groupName: String = "",
        var selectedProductIds: MutableList<Int> = mutableListOf<Int>(),
        var selectedKeywords: MutableList<String> = mutableListOf<String>(),
        var manualKeywords: MutableList<String> = mutableListOf<String>(),
        var selectedSuggestBid: MutableList<Int> = mutableListOf<Int>(),
        var minSuggestBidKeyword: Int = 0, // for keywords
        var suggestedBidPerClick: Int = 0, // for Default
        var finalBidPerClick: Int = -1, // Edited Bid by User
        var maxBid: Int = 0,
        var minBid: Int = 0,
        var dailyBudget: Int = 0,
        var adIds: MutableList<Int> = mutableListOf<Int>(),
        var adIdsPromo: MutableList<Int> = mutableListOf<Int>(),
        var adIdsNonPromo: MutableList<Int> = mutableListOf<Int>(),
        var selectedPromo: MutableList<Int> = mutableListOf<Int>(),
        var selectedNonPromo: MutableList<Int> = mutableListOf<Int>()) : StepperModel