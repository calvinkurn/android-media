package com.tokopedia.topads.dashboard.view.fragment

import android.support.v4.app.Fragment

import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Cell

/**
 * Created by hadi.putra on 17/05/18.
 */

class TopAdsStatisticSoldFragment : TopAdsDashboardStatisticFragment() {

    override val titleGraph: String
        get() = getString(R.string.title_top_ads_statistic_graph_sold)

    override fun getValueData(cell: Cell): Float = cell.soldSum

    override fun getValueDisplay(cell: Cell): String = cell.soldSumFmt

    companion object {
        fun createInstance(): Fragment = TopAdsStatisticSoldFragment()
    }
}
