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
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.di.DaggerDigitalPDPComponent
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPTagihanFragment
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPCategoryUtil
import com.tokopedia.digital_product_detail.presentation.utils.setupOrderListIcon
import com.tokopedia.header.HeaderUnify
import java.lang.ref.WeakReference


/**
 * @author by firmanda on 09/02/22
 * access applink tokopedia://digital/form?category_id=3&menu_id=305&template=tagihanplnv2
 * access internal applink tokopedia-android-internal://digital/pdp_tagihan_listrik
 */

class DigitalPDPTagihanActivity: BaseSimpleActivity(), HasComponent<DigitalPDPComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAppBar()
    }

    override fun getComponent(): DigitalPDPComponent {
        return DaggerDigitalPDPComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun getNewFragment(): Fragment? {
        val digitalTelcoExtraParam = TopupBillsExtraParam()
        val bundle = intent.extras
        digitalTelcoExtraParam.menuId = bundle?.getString(DigitalPDPConstant.PARAM_MENU_ID) ?: DigitalPDPCategoryUtil.DEFAULT_MENU_ID_TELCO
        digitalTelcoExtraParam.categoryId = bundle?.getString(DigitalPDPConstant.PARAM_CATEGORY_ID) ?: ""
        digitalTelcoExtraParam.productId = bundle?.getString(DigitalPDPConstant.PARAM_PRODUCT_ID) ?: ""
        digitalTelcoExtraParam.clientNumber = bundle?.getString(DigitalPDPConstant.PARAM_CLIENT_NUMBER) ?: ""
        return DigitalPDPTagihanFragment.newInstance(digitalTelcoExtraParam)
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
            val mActivity = WeakReference<Activity>(this@DigitalPDPTagihanActivity)
            setupOrderListIcon(mActivity)
            return true
        }
        return false
    }

    private fun setupAppBar() {
        (toolbar as HeaderUnify).transparentMode = true
    }
}