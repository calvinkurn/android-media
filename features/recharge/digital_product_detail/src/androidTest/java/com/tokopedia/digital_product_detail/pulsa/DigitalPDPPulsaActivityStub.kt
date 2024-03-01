package com.tokopedia.digital_product_detail.pulsa

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.digital_product_detail.di.DaggerDigitalPDPComponentStub
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.activity.DigitalPDPPulsaActivity

class DigitalPDPPulsaActivityStub: DigitalPDPPulsaActivity() {

    override fun getComponent(): DigitalPDPComponent {
        return DaggerDigitalPDPComponentStub
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }
}
