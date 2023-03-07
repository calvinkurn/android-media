package com.tokopedia.topads.common.view.adapter.keyword.viewmodel

import kotlinx.android.parcel.Parcelize

@Parcelize
data class HeadlineEditAdKeywordModel(val keywordName: String,
                                      var searchType: String = "",
                                      var advertisingCost: String,
                                      var priceBid: String = "0",
                                      val isNegativeKeyword: Boolean = false,
                                      var maximumBid: String = "0", var minimumBid: String = "0") : KeywordUiModel {
    override fun equals(other: Any?): Boolean {
        return other is HeadlineEditAdKeywordModel && other.keywordName == this.keywordName
    }

    override fun hashCode(): Int {
        return keywordName.hashCode()
    }
}