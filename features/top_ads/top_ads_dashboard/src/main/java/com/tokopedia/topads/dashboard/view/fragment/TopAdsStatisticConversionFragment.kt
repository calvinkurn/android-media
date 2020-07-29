package com.tokopedia.topads.dashboard.view.fragment

import androidx.fragment.app.Fragment
import com.tokopedia.topads.dashboard.data.model.Cell

/**
 * Created by zulfikarrahman on 1/6/17.
 */

class TopAdsStatisticConversionFragment : TopAdsDashboardStatisticFragment() {

    override fun getValueData(cell: Cell): Float = cell.conversionSum.toFloat()

    override fun getValueDisplay(cell: Cell): String = cell.conversionSumFmt

    companion object {
        fun createInstance(): Fragment = TopAdsStatisticConversionFragment()
    }
}
