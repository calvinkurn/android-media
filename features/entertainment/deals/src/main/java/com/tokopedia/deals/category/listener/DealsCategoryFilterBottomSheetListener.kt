package com.tokopedia.deals.category.listener

import com.tokopedia.deals.common.ui.dataview.DealsChipsDataView

interface DealsCategoryFilterBottomSheetListener {
    fun onFilterApplied(chips: DealsChipsDataView)
}