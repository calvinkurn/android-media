package com.tokopedia.digital_product_detail.presentation.activity

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.di.DaggerDigitalPDPComponent
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPPulsaFragment
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPTelcoUtil.DEFAULT_MENU_ID_TELCO
import com.tokopedia.digital_product_detail.presentation.utils.setupOrderListIcon
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.toBitmap
import java.lang.ref.WeakReference

/**
 * @author by firmanda on 04/01/21
 * tokopedia://digital/form?category_id=1&menu_id=148&template=pulsav2
 * access internal applink tokopedia-android-internal://digital/pdp_pulsa
 */


class DigitalPDPPulsaActivity: BaseSimpleActivity(), HasComponent<DigitalPDPComponent> {

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
            setupOrderListIcon(mActivity)
            return true
        }
        return false
    }

    private fun setupAppBar() {
        (toolbar as HeaderUnify).transparentMode = true
    }

    companion object {
        const val PARAM_MENU_ID = "menu_id"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_CLIENT_NUMBER = "client_number"
        const val PARAM_CATEGORY_ID = "category_id"
    }
}