package com.tokopedia.topads.dashboard.view.fragment


import android.support.v4.app.Fragment

import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Cell

/**
 * Created by zulfikarrahman on 1/6/17.
 */

class TopAdsStatisticSpentFragment : TopAdsDashboardStatisticFragment() {

    override val titleGraph: String
        get() = getString(R.string.title_top_ads_statistic_graph_spent)

    public override fun getValueData(cell: Cell): Float = cell.costSum

    override fun getValueDisplay(cell: Cell): String = getString(R.string.top_ads_tooltip_statistic_use, cell.costSumFmt)

    companion object {
        fun createInstance(): Fragment = TopAdsStatisticSpentFragment()
    }
}
