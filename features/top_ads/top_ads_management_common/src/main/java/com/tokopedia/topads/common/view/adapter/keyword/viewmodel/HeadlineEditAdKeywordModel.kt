package com.tokopedia.topads.common.view.adapter.keyword.viewmodel

import kotlinx.android.parcel.Parcelize

@Parcelize
data class HeadlineEditAdKeywordModel(val keywordName: String,
                                      var searchType: String = "",
                                      var advertisingCost: String,
                                      var priceBid: Int = 0,
                                      val isNegativeKeyword: Boolean = false) : KeywordUiModel