package com.tokopedia.statistic.presentation.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.view.fragment.StatisticFragment

/**
 * Created By @ilhamsuaib on 08/06/20
 *
 * Internal applink : ApplinkConstInternalMarketplace.MERCHANT_STATISTIC_DASHBOARD
 */

class StatisticActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stc_statistic)

        showFragment()
    }

    private fun showFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view_stc, StatisticFragment.newInstance())
                .commitNowAllowingStateLoss()
    }
}