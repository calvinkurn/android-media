package com.tokopedia.topupbills.telco.prepaid.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoComponent
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoInstance
import com.tokopedia.topupbills.telco.prepaid.fragment.DigitalTelcoPrepaidFragment

/**
 * applink
 * tokopedia://digital/form?category_id=1&menu_id=2&template=telcopre
 * or
 * RouteManager.route(this, ApplinkConsInternalDigital.PRODUCT_TEMPLATE, 17, 2, telcopre)
 */

class TelcoPrepaidActivity : BaseTelcoActivity(), HasComponent<DigitalTelcoComponent> {

    var categoryId = ""

    override fun getNewFragment(): Fragment? {
        val digitalTelcoExtraParam = TopupBillsExtraParam()
        val bundle = intent.extras
        digitalTelcoExtraParam.menuId = bundle?.getString(PARAM_MENU_ID)
                ?: TelcoComponentType.TELCO_PREPAID.toString()
        categoryId = bundle?.getString(PARAM_CATEGORY_ID) ?: ""
        digitalTelcoExtraParam.categoryId = categoryId
        digitalTelcoExtraParam.productId = bundle?.getString(PARAM_PRODUCT_ID)
                ?: ""
        digitalTelcoExtraParam.clientNumber = bundle?.getString(PARAM_CLIENT_NUMBER)
                ?: ""
        val rechargeProductFromSlice = bundle?.getString(RECHARGE_PRODUCT_EXTRA) ?: ""
        return DigitalTelcoPrepaidFragment.newInstance(digitalTelcoExtraParam,rechargeProductFromSlice)
    }

    override fun getComponent(): DigitalTelcoComponent? {
        return DigitalTelcoInstance.getComponent(application)
    }

    override fun sendTrackingDotsMenuTelco(userId: String) {
        topupAnalytics.eventClickDotsMenuTelco(categoryId, userId)
    }
}