package com.tokopedia.tokopedianow.home.presentation.activity

import android.os.Bundle
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.home.analytic.HomePageLoadTimeMonitoring
import com.tokopedia.tokopedianow.home.di.component.HomeComponent
import com.tokopedia.tokopedianow.home.di.factory.HomeComponentFactory
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment

class TokoNowHomeActivity : BaseTokoNowActivity(), HasComponent<HomeComponent> {

    var pageLoadTimeMonitoring: HomePageLoadTimeMonitoring? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        super.onCreate(savedInstanceState)
    }

    override fun getFragment() = TokoNowHomeFragment.newInstance()

    override fun getComponent(): HomeComponent {
        return HomeComponentFactory
            .instance.createComponent(application)
    }

    private fun initPerformanceMonitoring() {
        pageLoadTimeMonitoring = HomePageLoadTimeMonitoring()
        pageLoadTimeMonitoring?.initPerformanceMonitoring()
    }
}
