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
        var selectedProductIds: List<Int> = mutableListOf<Int>(),
        var selectedKeywords: List<String> = mutableListOf<String>(),
        var manualKeywords: List<String> = mutableListOf<String>(),
        var selectedSuggestBid: List<Int> = mutableListOf<Int>(),
        var minSuggestBidKeyword: Int = 0, // for keywords
        var suggestedBidPerClick: Int = 0, // for Default
        var finalBidPerClick: Int = -1, // Edited Bid by User
        var maxBid: Int = 0,
        var minBid: Int = 0,
        var dailyBudget: Int = 0,
        var adIds: List<Int> = mutableListOf<Int>(),
        var adIdsPromo: List<Int> = mutableListOf<Int>(),
        var adIdsNonPromo: List<Int> = mutableListOf<Int>(),
        var selectedPromo: List<Int> = mutableListOf<Int>(),
        var selectedNonPromo: List<Int> = mutableListOf<Int>()) : StepperModel