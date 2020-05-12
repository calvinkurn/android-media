package com.tokopedia.product.manage.feature.list.view.ui.tab

import com.tokopedia.sortfilter.SortFilterItem

data class SelectedTab(
    val filter: SortFilterItem? = null,
    val count: Int
)