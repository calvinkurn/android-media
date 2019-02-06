package com.tokopedia.topads.dashboard.view.fragment


import android.support.v4.app.Fragment

import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Cell

/**
 * Created by zulfikarrahman on 1/6/17.
 */

class TopAdsStatisticKlikFragment : TopAdsDashboardStatisticFragment() {

    override val titleGraph: String
        get() = getString(R.string.title_top_ads_statistic_graph_click)

    public override fun getValueData(cell: Cell): Float = cell.clickSum.toFloat()

    override fun getValueDisplay(cell: Cell): String = cell.clickSumFmt

    companion object {
        fun createInstance(): Fragment = TopAdsStatisticKlikFragment()
    }
}
