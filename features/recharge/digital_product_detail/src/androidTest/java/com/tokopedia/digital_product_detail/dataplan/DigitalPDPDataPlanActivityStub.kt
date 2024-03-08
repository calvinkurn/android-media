package com.tokopedia.digital_product_detail.dataplan

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.digital_product_detail.di.DaggerDigitalPDPComponentStub
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.activity.DigitalPDPDataPlanActivity

class DigitalPDPDataPlanActivityStub: DigitalPDPDataPlanActivity() {

    override fun getComponent(): DigitalPDPComponent {
        return DaggerDigitalPDPComponentStub
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }
}
