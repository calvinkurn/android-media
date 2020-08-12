package com.tokopedia.deals.common.listener

import com.tokopedia.deals.common.ui.dataview.ChipDataView

interface DealChipsListActionListener {
    fun onFilterChipClicked(chips: List<ChipDataView>)
    fun onChipClicked(chips: List<ChipDataView>)
}