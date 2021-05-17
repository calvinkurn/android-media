package com.tokopedia.tokomart.searchcategory.presentation.model

import com.tokopedia.filter.common.data.Option
import com.tokopedia.sortfilter.SortFilterItem

data class SortFilterItemDataView(
        val option: Option = Option(),
        val sortFilterItem: SortFilterItem,
)