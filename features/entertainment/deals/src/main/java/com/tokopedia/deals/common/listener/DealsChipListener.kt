package com.tokopedia.deals.common.listener

import android.view.View
import com.tokopedia.deals.common.ui.dataview.ChipDataView

interface DealsChipListener {
    fun onChipClicked(itemView: View, chip: ChipDataView, position: Int)
}