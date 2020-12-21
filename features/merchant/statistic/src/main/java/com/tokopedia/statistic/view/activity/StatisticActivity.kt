package com.tokopedia.statistic.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.statistic.R
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoring
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringInterface
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringListener
import com.tokopedia.statistic.view.fragment.StatisticFragment

/**
 * Created By @ilhamsuaib on 08/06/20
 */

// Internal applink : ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD

class StatisticActivity : BaseActivity(), StatisticPerformanceMonitoringListener {

    private val statisticFragment by lazy { StatisticFragment.newInstance() }
    private val performanceMonitoring: StatisticPerformanceMonitoringInterface by lazy {
        StatisticPerformanceMonitoring()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stc_statistic)

        showFragment()
        setWhiteStatusBar()
    }

    override fun startNetworkPerformanceMonitoring() {
        performanceMonitoring.startNetworkPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        performanceMonitoring.startRenderPerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        performanceMonitoring.stopPerformanceMonitoring()
    }

    private fun showFragment() {
        val containerViewId = R.id.parent_view_stc
        val isFragmentNotAttachedYet = supportFragmentManager.findFragmentById(containerViewId) == null
        if (isFragmentNotAttachedYet) {
            supportFragmentManager.beginTransaction()
                    .replace(containerViewId, statisticFragment)
                    .commit()
        }
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            setLightStatusBar(true)
        }
    }

    private fun initPerformanceMonitoring() {
        performanceMonitoring.initPerformanceMonitoring()
    }
}