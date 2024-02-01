package com.tokopedia.digital_product_detail.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

class DigitalPDPComponentInstanceStub {

    fun getDigitalPDPComponent(application: Application): DigitalPDPComponent {
        return DaggerDigitalPDPComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }
}
