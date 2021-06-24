package com.tokopedia.tokomart.searchcategory.presentation.model

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.sortfilter.SortFilterItem

data class SortFilterItemDataView(
        val filter: Filter = Filter(),
        val sortFilterItem: SortFilterItem,
) {

    val firstOption: Option?
        get() = filter.options.firstOrNull()
}