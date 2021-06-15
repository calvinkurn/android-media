package com.tokopedia.review.feature.reading.presentation.listener

import com.tokopedia.unifycomponents.list.ListItemUnify

interface ReadReviewFilterBottomSheetListener {
    fun onFilterSubmitted(selectedFilter: List<ListItemUnify>)
}