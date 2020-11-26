package com.tokopedia.topads.dashboard.view.fragment

import androidx.fragment.app.Fragment

import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.Cell

/**
 * Created by hadi.putra on 17/05/18.
 */

class TopAdsStatisticIncomeFragment : TopAdsDashboardStatisticFragment() {

    override fun getValueData(cell: Cell): Float = cell.grossProfit

    override fun getValueDisplay(cell: Cell): String = getString(R.string.top_ads_tooltip_statistic_use, cell.grossProfitFmt)

    companion object {
        fun createInstance(): Fragment = TopAdsStatisticIncomeFragment()
    }
}
