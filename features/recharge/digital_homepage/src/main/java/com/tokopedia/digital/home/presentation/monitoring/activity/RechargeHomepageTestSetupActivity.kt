package com.tokopedia.digital.home.presentation.monitoring.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.digital.home.di.RechargeHomepageComponent
import com.tokopedia.digital.home.di.RechargeHomepageComponentInstance
import com.tokopedia.digital.home.presentation.fragment.RechargeHomepageFragment
import com.tokopedia.digital.home.presentation.monitoring.callback.RechargeHomepagePerformanceCallback
import javax.inject.Inject

class RechargeHomepageTestSetupActivity: BaseSimpleActivity(), HasComponent<RechargeHomepageComponent> {

    @Inject
    lateinit var performanceMonitoring: RechargeHomepagePerformanceCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        rechargeHomepageComponent().inject(this)
        startPageMonitoring()
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment {
        val platformId = 53
        val enablePersonalize = false
        val sliceOpenApp = false
        return RechargeHomepageFragment.newInstance(platformId, enablePersonalize, sliceOpenApp)
    }

    override fun getComponent(): RechargeHomepageComponent {
        return rechargeHomepageComponent()
    }

    private fun rechargeHomepageComponent(): RechargeHomepageComponent {
        return RechargeHomepageComponentInstance.getRechargeHomepageComponent(application)
    }

    override fun onBackPressed() {
        if(fragment != null){
            val rechargeFragment = (fragment as com.tokopedia.digital.home.presentation.fragment.RechargeHomepageFragment)
            rechargeFragment.onBackPressed()
        }
        super.onBackPressed()
    }

    private fun startPageMonitoring() {
        performanceMonitoring.startPerformanceMonitoring()
        performanceMonitoring.startPreparePagePerformanceMonitoring()
    }
}
