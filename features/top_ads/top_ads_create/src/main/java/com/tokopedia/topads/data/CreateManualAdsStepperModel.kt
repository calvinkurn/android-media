package com.tokopedia.topads.data

import com.tokopedia.abstraction.base.view.model.StepperModel
import com.tokopedia.topads.common.data.response.KeywordDataItem
import kotlinx.parcelize.Parcelize
import com.tokopedia.topads.common.data.response.TopAdsProductModel

/**
 * Author errysuprayogi on 01,November,2019
 */
@Parcelize
open class CreateManualAdsStepperModel(
    var groupName: String = "",
    var selectedProductIds: MutableList<String> = mutableListOf(),
    var selectedKeywordStage: MutableList<KeywordDataItem> = mutableListOf(),
    var minSuggestBidKeyword: String = "0", // for keywords
    var suggestedBidPerClick: String = "0", // for Default
    var finalSearchBidPerClick: Int = -1, // Edited Bid by User
    var finalRecommendationBidPerClick: Int = -1, // Edited Bid by User
    var maxBid: String = "0",
    var minBid: String = "0",
    var dailyBudget: Int = 0,
    var adIds: MutableList<String> = mutableListOf(),
    var adIdsPromo: MutableList<String> = mutableListOf(),
    var adIdsNonPromo: MutableList<String> = mutableListOf(),
    var selectedPromo: MutableList<String> = mutableListOf(),
    var redirectionToSummary: Boolean = false,
    var goToSummary: Boolean = false,
    var autoBidState: String = "auto_bid",
    var selectedNonPromo: MutableList<String> = mutableListOf(),
    var recomPrediction: Int = 0,
    var searchPrediction: Int = 0,
    var productList: MutableList<TopAdsProductModel> = mutableListOf(),
    var productListPromo: MutableList<TopAdsProductModel> = mutableListOf(),
    var productListNonPromo: MutableList<TopAdsProductModel> = mutableListOf()
) : StepperModel
