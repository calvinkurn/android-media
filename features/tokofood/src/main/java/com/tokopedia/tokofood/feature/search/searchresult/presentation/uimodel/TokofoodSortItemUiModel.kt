package com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel

import com.tokopedia.filter.common.data.Sort
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sortfilter.SortFilterItem

data class TokofoodSortItemUiModel(
    override val sortFilterItem: SortFilterItem,
    override var totalSelectedOptions: Int,
    override var selectedKey: String?,
    val sortList: List<Sort>,
    val selectedSort: Sort? = null
): ImpressHolder(), TokofoodSortFilterItemUiModel