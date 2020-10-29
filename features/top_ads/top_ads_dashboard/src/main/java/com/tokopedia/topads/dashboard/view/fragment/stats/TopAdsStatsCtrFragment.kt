package com.tokopedia.topads.dashboard.view.fragment.stats

import androidx.fragment.app.Fragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsDashStatisticFragment

/**
 * Created by Pika on 26/10/20.
 */

class TopAdsStatsCtrFragment : TopAdsDashStatisticFragment() {


    companion object {
        fun createInstance(): Fragment = TopAdsStatsCtrFragment()
    }

    override fun getIndex(): Int {
        return 4
    }
}
