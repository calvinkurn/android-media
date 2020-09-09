package com.tokopedia.statistic.presentation.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.setLightStatusBar
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.statistic.R
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoring
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringInterface
import com.tokopedia.statistic.analytics.performance.StatisticPerformanceMonitoringListener
import com.tokopedia.statistic.presentation.view.fragment.StatisticFragment

/**
 * Created By @ilhamsuaib on 08/06/20
 */

// Internal applink : ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD

class StatisticActivity : BaseSimpleActivity(), StatisticPerformanceMonitoringListener {

    private val performanceMonitoring: StatisticPerformanceMonitoringInterface by lazy {
        StatisticPerformanceMonitoring()
    }

    private val statisticFragment by lazy {
        StatisticFragment.newInstance()
    }

    override fun getNewFragment(): Fragment? {
        return statisticFragment
    }

    override fun getParentViewResourceID(): Int = R.id.parent_view_stc

    override fun getLayoutRes(): Int = R.layout.activity_stc_statistic

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stc_statistic)
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