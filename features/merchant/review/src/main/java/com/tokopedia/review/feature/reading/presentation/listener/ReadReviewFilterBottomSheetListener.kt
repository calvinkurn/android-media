package com.tokopedia.review.feature.reading.presentation.listener

import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterBottomSheetType
import com.tokopedia.unifycomponents.list.ListItemUnify

interface ReadReviewFilterBottomSheetListener {
    fun onFilterSubmitted(selectedFilter: Set<ListItemUnify>, filterType: SortFilterBottomSheetType, index: Int)
    fun onSortSubmitted(selectedSort: ListItemUnify)
}