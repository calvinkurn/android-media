package com.tokopedia.topads.dashboard.view.fragment

import android.support.v4.app.Fragment

import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Cell

/**
 * Created by hadi.putra on 17/05/18.
 */

class TopAdsStatisticIncomeFragment : TopAdsDashboardStatisticFragment() {

    override val titleGraph: String
        get() = getString(R.string.title_top_ads_statistic_graph_total_income)

    override fun getValueData(cell: Cell): Float = cell.grossProfit

    override fun getValueDisplay(cell: Cell): String = getString(R.string.top_ads_tooltip_statistic_use, cell.grossProfitFmt)

    companion object {
        fun createInstance(): Fragment = TopAdsStatisticIncomeFragment()
    }
}
