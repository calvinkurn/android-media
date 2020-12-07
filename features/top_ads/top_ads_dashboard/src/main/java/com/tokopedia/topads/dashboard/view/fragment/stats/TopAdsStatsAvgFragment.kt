package com.tokopedia.topads.dashboard.view.fragment.stats

import androidx.fragment.app.Fragment
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_6
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashStatisticFragment

/**
 * Created by Pika on 26/10/20.
 */

class TopAdsStatsAvgFragment : TopAdsDashStatisticFragment() {


    companion object {
        fun createInstance(): Fragment = TopAdsStatsAvgFragment()
    }

    override fun getIndex(): Int {
        return CONST_6
    }
}
