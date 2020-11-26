package com.tokopedia.review.feature.inboxreview.presentation.model

import com.tokopedia.sortfilter.SortFilterItem

data class SortFilterInboxItemWrapper(
        val sortFilterItem: SortFilterItem? = null,
        var isSelected: Boolean = false,
        var count: Int = 0,
        var sortValue: String = ""
)