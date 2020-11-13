package com.tokopedia.top_ads_headline.data

import com.tokopedia.abstraction.base.view.model.StepperModel
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.sdk.domain.model.CpmModel
import kotlinx.android.parcel.Parcelize

@Parcelize
open class CreateHeadlineAdsStepperModel(
        var groupName: String = "",
        var selectedProductIds: MutableList<Int> = mutableListOf(),
        var selectedKeywords: MutableList<KeywordDataItem> = mutableListOf(),
        var manualSelectedKeywords: MutableList<KeywordDataItem> = mutableListOf(),
        var slogan: String = "",
        var cpmModel: CpmModel = CpmModel(),
        var minBid: Int = 0,
        var dailyBudget: Int = 0,
        var adOperations: MutableList<TopAdsManageHeadlineInput.Operation.Group.AdOperation> = ArrayList(),
        var keywordOperations: List<TopAdsManageHeadlineInput.Operation.Group.KeywordOperation> = ArrayList()
) : StepperModel
