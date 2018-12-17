package com.tokopedia.topads.dashboard.view.fragment


import android.support.v4.app.Fragment

import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Cell

/**
 * Created by zulfikarrahman on 1/6/17.
 */

class TopAdsStatisticImprFragment : TopAdsDashboardStatisticFragment() {

    override val titleGraph: String
        get() = getString(R.string.title_top_ads_statistic_graph_impression)

    override fun getValueData(cell: Cell): Float = cell.impressionSum.toFloat()

    override fun getValueDisplay(cell: Cell): String = cell.impressionSumFmt

    companion object {
        fun createInstance(): Fragment = TopAdsStatisticImprFragment()
    }
}
