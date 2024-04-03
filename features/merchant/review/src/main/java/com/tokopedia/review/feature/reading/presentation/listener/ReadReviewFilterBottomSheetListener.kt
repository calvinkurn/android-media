package com.tokopedia.review.feature.reading.presentation.listener

import com.tokopedia.review.feature.reading.presentation.uimodel.SortFilterBottomSheetType
import com.tokopedia.review.feature.reading.presentation.widget.SelectVariantUiModel
import com.tokopedia.unifycomponents.list.ListItemUnify

interface ReadReviewFilterBottomSheetListener {
    fun onFilterSubmitted(filterName: String, selectedFilter: Set<ListItemUnify>, filterType: SortFilterBottomSheetType, index: Int)
    fun onSortSubmitted(selectedSort: ListItemUnify)
    fun onFilterVariant(count: Int, variantFilter: String, variants: List<SelectVariantUiModel.Variant>)
}
