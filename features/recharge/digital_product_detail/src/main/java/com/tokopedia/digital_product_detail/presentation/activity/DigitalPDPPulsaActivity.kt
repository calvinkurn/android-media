package com.tokopedia.digital_product_detail.presentation.activity

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.PARAM_CATEGORY_ID
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.PARAM_CLIENT_NUMBER
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.PARAM_MENU_ID
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant.PARAM_PRODUCT_ID
import com.tokopedia.digital_product_detail.di.DaggerDigitalPDPComponent
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPPulsaFragment
import com.tokopedia.digital_product_detail.presentation.listener.DigitalHistoryIconListener
import com.tokopedia.digital_product_detail.presentation.monitoring.DigitalPDPPulsaPerformanceCallback
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPCategoryUtil.DEFAULT_MENU_ID_TELCO
import com.tokopedia.digital_product_detail.presentation.utils.setupOrderListIcon
import com.tokopedia.header.HeaderUnify
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * @author by firmanda on 04/01/22
 * tokopedia://digital/form?category_id=1&menu_id=148&template=pulsav2
 * tokopedia://digital/form?category_id=1&menu_id=289&operator_id=5&product_id=32&client_number=087855812081&template=pulsav2
 * access internal applink tokopedia-android-internal://digital/pdp_pulsa
 */

open class DigitalPDPPulsaActivity: BaseSimpleActivity(), HasComponent<DigitalPDPComponent> {

    @Inject
    lateinit var performanceMonitoring: DigitalPDPPulsaPerformanceCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        getDaggerComponent().inject(this)
        startPageMonitoring()
        super.onCreate(savedInstanceState)
        setupAppBar()
    }

    override fun getComponent(): DigitalPDPComponent {
        return getDaggerComponent()
    }

    override fun getNewFragment(): Fragment {
        val digitalTelcoExtraParam = TopupBillsExtraParam()
        val bundle = intent.extras
        digitalTelcoExtraParam.menuId = bundle?.getString(PARAM_MENU_ID) ?: DEFAULT_MENU_ID_TELCO
        digitalTelcoExtraParam.categoryId = bundle?.getString(PARAM_CATEGORY_ID) ?: ""
        digitalTelcoExtraParam.productId = bundle?.getString(PARAM_PRODUCT_ID) ?: ""
        digitalTelcoExtraParam.clientNumber = bundle?.getString(PARAM_CLIENT_NUMBER) ?: ""
        return DigitalPDPPulsaFragment.newInstance(digitalTelcoExtraParam)
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
            val mActivity = WeakReference<Activity>(this@DigitalPDPPulsaActivity)
            setupOrderListIcon(mActivity, (fragment as DigitalHistoryIconListener))
            return true
        }
        return false
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
