package com.tokopedia.tokofood.feature.search.srp.presentation.uimodel

import com.tokopedia.sortfilter.SortFilterItem

interface TokofoodSortFilterItemUiModel {
    val sortFilterItem: SortFilterItem
    var isSelected: Boolean
}