package com.tokopedia.digital_product_detail.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.digital_product_detail.di.DaggerDigitalPDPComponent
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.fragment.DigitalPDPDataPlanFragment

/**
 * @author by firmanda on 04/01/21
 * access applink
 * access internal applink tokopedia-android-internal://digital/pdp_paket_data
 */

class DigitalPDPDataPlanActivity: BaseSimpleActivity(), HasComponent<DigitalPDPComponent> {

    override fun getComponent(): DigitalPDPComponent {
        return DaggerDigitalPDPComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun getNewFragment(): Fragment {
        return DigitalPDPDataPlanFragment.newInstance()
    }
}