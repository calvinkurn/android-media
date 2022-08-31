package com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.sortfilter.SortFilterItem

class TokofoodFilterItemUiModel(
    override val sortFilterItem: SortFilterItem,
    override var isSelected: Boolean,
    val filter: Filter
): TokofoodSortFilterItemUiModel