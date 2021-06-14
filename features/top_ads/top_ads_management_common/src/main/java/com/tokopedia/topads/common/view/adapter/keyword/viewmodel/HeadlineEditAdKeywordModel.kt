package com.tokopedia.topads.common.view.adapter.keyword.viewmodel

import kotlinx.android.parcel.Parcelize

@Parcelize
data class HeadlineEditAdKeywordModel(val keywordName: String,
                                      var searchType: String = "",
                                      var advertisingCost: String,
                                      var priceBid: String = "0",
                                      val isNegativeKeyword: Boolean = false,
                                      var maximumBid: Float = 0.0f, var minimumBid: Float = 0.0f) : KeywordUiModel {
    override fun equals(other: Any?): Boolean {
        return other is HeadlineEditAdKeywordModel && other.keywordName == this.keywordName
    }

    override fun hashCode(): Int {
        return keywordName.hashCode()
    }
}