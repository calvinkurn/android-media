package com.tokopedia.statistic.presentation.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.view.fragment.StatisticFragment

/**
 * Created By @ilhamsuaib on 08/06/20
 */

// Internal applink : ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD

class StatisticActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stc_statistic)

        showFragment()
        setWhiteStatusBar()
    }

    private fun showFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view_stc, StatisticFragment.newInstance())
                .commitNowAllowingStateLoss()
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            setLightStatusBar(true)
        }
    }
}