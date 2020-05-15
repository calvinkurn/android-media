package com.tokopedia.topupbills.telco.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.view.di.DigitalTopupComponent
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoPrepaidFragment
import timber.log.Timber

/**
 * applink
 * tokopedia://digital/form?category_id=1&menu_id=2&template=telcopre
 * or
 * RouteManager.route(this, ApplinkConsInternalDigital.PRODUCT_TEMPLATE, 17, 2, telcopre)
 */

class TelcoPrepaidActivity : BaseTelcoActivity(), HasComponent<DigitalTopupComponent> {

    override fun getNewFragment(): Fragment? {
        val digitalTelcoExtraParam = TopupBillsExtraParam()
        val bundle = intent.extras
        digitalTelcoExtraParam.menuId = bundle?.getString(PARAM_MENU_ID)
                ?: TelcoComponentType.TELCO_PREPAID.toString()
        digitalTelcoExtraParam.categoryId = bundle?.getString(PARAM_CATEGORY_ID)
                ?: ""
        digitalTelcoExtraParam.productId = bundle?.getString(PARAM_PRODUCT_ID)
                ?: ""
        digitalTelcoExtraParam.clientNumber = bundle?.getString(PARAM_CLIENT_NUMBER)
                ?: ""
        return DigitalTelcoPrepaidFragment.newInstance(digitalTelcoExtraParam)
    }

    override fun getComponent(): DigitalTopupComponent {
        return DigitalTopupInstance.getComponent(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.handleExtra()
    }

    /* This Method is use to tracking Action click when user click TelcoProduct
   */

    private fun Intent.handleExtra() {
        if (intent.data != null) {
            val trackingClick = intent.getStringExtra(RECHARGE_PRODUCT_EXTRA)
            if (trackingClick != null) {
                Timber.w("P2#ACTION_SLICE_CLICK_RECHARGE#$trackingClick")
            }
        }
    }

    companion object {
        const val RECHARGE_PRODUCT_EXTRA = "RECHARGE_PRODUCT_EXTRA"
    }
}