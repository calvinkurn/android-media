package com.tokopedia.digital_product_detail.presentation.monitoring.activity

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.di.DaggerDigitalPDPComponent
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPDataPlanFragment
import com.tokopedia.digital_product_detail.presentation.listener.DigitalHistoryIconListener
import com.tokopedia.digital_product_detail.presentation.monitoring.DigitalPDPDataPlanPerformanceCallback
import com.tokopedia.digital_product_detail.presentation.utils.setupOrderListIcon
import com.tokopedia.header.HeaderUnify
import java.lang.ref.WeakReference
import javax.inject.Inject

class DigitalPDPDataPlanTestSetupActivity: BaseSimpleActivity(), HasComponent<DigitalPDPComponent> {

    @Inject
    lateinit var performanceMonitoring: DigitalPDPDataPlanPerformanceCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        getDaggerComponent().inject(this)
        startPageMonitoring()
        super.onCreate(savedInstanceState)
        setupAppBar()
    }

    override fun getNewFragment(): Fragment? {
        val digitalTelcoExtraParam = TopupBillsExtraParam()
        digitalTelcoExtraParam.menuId = "290"
        digitalTelcoExtraParam.categoryId = "2"
        digitalTelcoExtraParam.clientNumber = "081208120812"

        return DigitalPDPDataPlanFragment.newInstance(digitalTelcoExtraParam)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_pdp
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.pdp_toolbar
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.run {
            val mActivity = WeakReference<Activity>(this@DigitalPDPDataPlanTestSetupActivity)
            setupOrderListIcon(mActivity, (fragment as DigitalHistoryIconListener))
            return true
        }
        return false
    }

    override fun getComponent(): DigitalPDPComponent {
        return getDaggerComponent()
    }

    private fun setupAppBar() {
        (toolbar as HeaderUnify).transparentMode = true
    }

    private fun getDaggerComponent(): DigitalPDPComponent =
        DaggerDigitalPDPComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()

    private fun startPageMonitoring() {
        performanceMonitoring.startPerformanceMonitoring()
        performanceMonitoring.startPreparePagePerformanceMonitoring()
    }
}
