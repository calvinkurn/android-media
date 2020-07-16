package com.tokopedia.deals.common.listener

import com.tokopedia.deals.common.ui.dataview.DealsChipsDataView

interface DealChipsListActionListener {
    fun onFilterChipClicked(chips: DealsChipsDataView)
    fun onChipClicked(chips: DealsChipsDataView)
}