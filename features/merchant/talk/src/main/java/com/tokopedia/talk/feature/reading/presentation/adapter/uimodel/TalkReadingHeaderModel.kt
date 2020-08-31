package com.tokopedia.talk.feature.reading.presentation.adapter.uimodel

import com.tokopedia.sortfilter.SortFilterItem

data class TalkReadingHeaderModel(
        val productName: String = "",
        val productImageUrl: String = "",
        val categories: ArrayList<SortFilterItem> = arrayListOf()
)