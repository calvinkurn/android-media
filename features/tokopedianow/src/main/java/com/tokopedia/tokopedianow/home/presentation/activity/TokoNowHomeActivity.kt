package com.tokopedia.tokopedianow.home.presentation.activity

import android.os.Bundle
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.home.analytic.HomePageLoadTimeMonitoring
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment

class TokoNowHomeActivity : BaseTokoNowActivity() {

    var pageLoadTimeMonitoring: HomePageLoadTimeMonitoring? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        super.onCreate(savedInstanceState)
    }

    override fun getFragment() = TokoNowHomeFragment.newInstance()

    private fun initPerformanceMonitoring() {
        pageLoadTimeMonitoring = HomePageLoadTimeMonitoring()
        pageLoadTimeMonitoring?.initPerformanceMonitoring()
    }
}