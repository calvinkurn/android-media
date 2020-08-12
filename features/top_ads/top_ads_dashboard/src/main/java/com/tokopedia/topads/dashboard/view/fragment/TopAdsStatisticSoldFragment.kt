package com.tokopedia.topads.dashboard.view.fragment

import androidx.fragment.app.Fragment

import com.tokopedia.topads.dashboard.data.model.Cell

/**
 * Created by hadi.putra on 17/05/18.
 */

class TopAdsStatisticSoldFragment : TopAdsDashboardStatisticFragment() {

    override fun getValueData(cell: Cell): Float = cell.soldSum

    override fun getValueDisplay(cell: Cell): String = cell.soldSumFmt

    companion object {
        fun createInstance(): Fragment = TopAdsStatisticSoldFragment()
    }
}
