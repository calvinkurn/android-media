package com.tokopedia.reviewseller.feature.inboxreview.presentation.model

import com.tokopedia.sortfilter.SortFilterItem

data class SortFilterItemInboxWrapper(
        val sortFilterItem: SortFilterItem? = null,
        var isSelected: Boolean = false,
        val count: Int = 0,
        val sortValue: String = ""
)