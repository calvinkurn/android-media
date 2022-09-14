package com.tokopedia.tokofood.feature.search.searchresult.presentation.uimodel

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sortfilter.SortFilterItem

data class TokofoodFilterItemUiModel(
    override val sortFilterItem: SortFilterItem,
    override var totalSelectedOptions: Int,
    override var selectedKey: String?,
    val filter: Filter
): ImpressHolder(), TokofoodSortFilterItemUiModel