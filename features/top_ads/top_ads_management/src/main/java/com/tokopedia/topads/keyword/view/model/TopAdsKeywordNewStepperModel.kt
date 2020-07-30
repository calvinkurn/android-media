package com.tokopedia.topads.keyword.view.model

import com.tokopedia.abstraction.base.view.model.StepperModel
import com.tokopedia.topads.keyword.constant.KeywordTypeDef
import kotlinx.android.parcel.Parcelize

@Parcelize
open class TopAdsKeywordNewStepperModel(
        var isPositive: Boolean = false,
        var groupId: String? = null,
        var choosenId: String? = null,
        var groupName: String? = null,
        @KeywordTypeDef
        var keywordType: Int = 0,
        var serverCount: Int = 0,
        var maxWords: Int = 0,
        var localWords: MutableList<String> = mutableListOf(),
        var priceBid: Int = 0) : StepperModel