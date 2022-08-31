package com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel

import com.tokopedia.filter.common.data.Sort
import com.tokopedia.sortfilter.SortFilterItem

class TokofoodSortItemUiModel(
    override val sortFilterItem: SortFilterItem,
    override var isSelected: Boolean,
    val sortList: List<Sort>
): TokofoodSortFilterItemUiModel