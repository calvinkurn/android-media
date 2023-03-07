package com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel

import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleStatusFilter

data class TabConfig(
    val filters: ArrayList<SortFilterItem>,
    val statusFilters: List<FlashSaleStatusFilter>,
    val emptyStateConfig: EmptyStateConfig
)

