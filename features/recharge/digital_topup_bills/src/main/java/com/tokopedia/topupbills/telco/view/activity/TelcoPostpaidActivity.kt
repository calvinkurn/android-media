package com.tokopedia.topupbills.telco.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.view.di.DigitalTopupComponent
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.view.fragment.DigitalTelcoPostpaidFragment

/**
 * applink
 * tokopedia://digital/form?category_id=9&menu_id=3&template=telcopost
 * or
 * RouteManager.route(this, ApplinkConsInternalDigital.PRODUCT_TEMPLATE, 17, 3, telcopost)
 */

class TelcoPostpaidActivity : BaseTelcoActivity(), HasComponent<DigitalTopupComponent> {

    override fun getNewFragment(): Fragment? {
        val digitalTelcoExtraParam = TopupBillsExtraParam()
        val bundle = intent.extras
        digitalTelcoExtraParam.menuId = bundle?.getString(PARAM_MENU_ID)
                ?: TelcoComponentType.TELCO_POSTPAID.toString()
        digitalTelcoExtraParam.categoryId = bundle?.getString(PARAM_CATEGORY_ID)
                ?: ""
        digitalTelcoExtraParam.productId = bundle?.getString(PARAM_PRODUCT_ID)
                ?: ""
        digitalTelcoExtraParam.clientNumber = bundle?.getString(PARAM_CLIENT_NUMBER)
                ?: ""
        return DigitalTelcoPostpaidFragment.newInstance(digitalTelcoExtraParam)
    }

    override fun getComponent(): DigitalTopupComponent {
        return DigitalTopupInstance.getComponent(application)
    }
}