package com.tokopedia.topupbills.telco.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.view.di.DigitalTopupComponent
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoPostpaidFragment

/**
 * applink
 * tokopedia://digital/form?category_id=1&menu_id=2&template=telcopost
 * or
 * RouteManager.route(this, ApplinkConsInternalDigital.PRODUCT_TEMPLATE, 17, 2, telcopost)
 */

class TelcoPostpaidActivity : BaseTelcoActivity(), HasComponent<DigitalTopupComponent> {

    override fun getNewFragment(): Fragment? {
        val digitalTelcoExtraParam = TopupBillsExtraParam()
        val bundle = intent.extras
        digitalTelcoExtraParam.menuId = bundle?.getString(TelcoProductActivity.PARAM_MENU_ID)
                ?: TelcoComponentType.TELCO_POSTPAID.toString()
        digitalTelcoExtraParam.categoryId = bundle?.getString(TelcoProductActivity.PARAM_CATEGORY_ID)
                ?: ""
        digitalTelcoExtraParam.productId = bundle?.getString(TelcoProductActivity.PARAM_PRODUCT_ID)
                ?: ""
        digitalTelcoExtraParam.clientNumber = bundle?.getString(TelcoProductActivity.PARAM_CLIENT_NUMBER)
                ?: ""
        return DigitalTelcoPostpaidFragment.newInstance(digitalTelcoExtraParam)
    }

    override fun getComponent(): DigitalTopupComponent {
        return DigitalTopupInstance.getComponent(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.digital_title_prepaid_page))
    }
}