package com.tokopedia.filter.bottomsheet.filtergeneraldetail

import com.tokopedia.filter.common.data.Sort

interface SortListener {
    fun onSortOptionClick(sort: Sort)

    fun onApplySort(sort: Sort)
}
