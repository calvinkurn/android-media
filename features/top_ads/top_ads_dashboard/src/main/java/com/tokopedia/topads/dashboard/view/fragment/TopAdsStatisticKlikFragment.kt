package com.tokopedia.topads.dashboard.view.fragment


import androidx.fragment.app.Fragment

import com.tokopedia.topads.dashboard.data.model.Cell

/**
 * Created by zulfikarrahman on 1/6/17.
 */

class TopAdsStatisticKlikFragment : TopAdsDashboardStatisticFragment() {

    public override fun getValueData(cell: Cell): Float = cell.clickSum.toFloat()

    override fun getValueDisplay(cell: Cell): String = cell.clickSumFmt

    companion object {
        fun createInstance(): Fragment = TopAdsStatisticKlikFragment()
    }
}
