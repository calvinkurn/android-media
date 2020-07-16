package com.tokopedia.topads.dashboard.view.fragment

import androidx.fragment.app.Fragment

import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Cell

/**
 * Created by zulfikarrahman on 1/6/17.
 */

class TopAdsStatisticSpentFragment : TopAdsDashboardStatisticFragment() {

    public override fun getValueData(cell: Cell): Float = cell.costSum

    override fun getValueDisplay(cell: Cell): String = getString(R.string.top_ads_tooltip_statistic_use, cell.costSumFmt)

    companion object {
        fun createInstance(): Fragment = TopAdsStatisticSpentFragment()
    }
}
