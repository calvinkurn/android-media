package com.tokopedia.digital_product_detail.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.di.DaggerDigitalPDPComponent
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPDataPlanFragment
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPPulsaFragment
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPTelcoUtil

/**
 * @author by firmanda on 04/01/21
 * access applink tokopedia://digital/form?category_id=2&menu_id=290&template=paketdatav2
 * access internal applink tokopedia-android-internal://digital/pdp_paket_data
 */

class DigitalPDPDataPlanActivity: BaseSimpleActivity(), HasComponent<DigitalPDPComponent> {

    override fun getComponent(): DigitalPDPComponent {
        return DaggerDigitalPDPComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun getNewFragment(): Fragment {
        val digitalTelcoExtraParam = TopupBillsExtraParam()
        val bundle = intent.extras
        digitalTelcoExtraParam.menuId = bundle?.getString(DigitalPDPConstant.PARAM_MENU_ID) ?: DigitalPDPTelcoUtil.DEFAULT_MENU_ID_TELCO
        digitalTelcoExtraParam.categoryId = bundle?.getString(DigitalPDPConstant.PARAM_CATEGORY_ID) ?: ""
        digitalTelcoExtraParam.productId = bundle?.getString(DigitalPDPConstant.PARAM_PRODUCT_ID) ?: ""
        digitalTelcoExtraParam.clientNumber = bundle?.getString(DigitalPDPConstant.PARAM_CLIENT_NUMBER) ?: ""
        return DigitalPDPDataPlanFragment.newInstance(digitalTelcoExtraParam)
    }
}