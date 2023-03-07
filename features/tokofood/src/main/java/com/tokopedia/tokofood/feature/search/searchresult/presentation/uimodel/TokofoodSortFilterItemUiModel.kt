package com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel

import com.tokopedia.sortfilter.SortFilterItem

interface TokofoodSortFilterItemUiModel {
    val sortFilterItem: SortFilterItem
    var totalSelectedOptions: Int
    var selectedKey: String?
}