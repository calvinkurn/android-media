package com.tokopedia.filter.bottomsheet.sort

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Sort

internal class SortItemViewModel(
        val sort: Sort
) {
    var isSelected = false

    fun isDefaultSort() = sort.value == SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
}