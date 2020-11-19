package com.tokopedia.topads.dashboard.view.fragment.stats

import androidx.fragment.app.Fragment
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.CONST_0
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashStatisticFragment

/**
 * Created by Pika on 26/10/20.
 */

class TopAdsStatsImprFragment : TopAdsDashStatisticFragment() {

    override fun getIndex(): Int {
       return CONST_0
    }

    companion object {
        fun createInstance(): Fragment = TopAdsStatsImprFragment()
    }
}
