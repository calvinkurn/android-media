package com.tokopedia.recharge_pdp_emoney.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common_digital.common.di.DaggerDigitalCommonComponent


object EmoneyPdpComponentInstance {
    private lateinit var emoneyPdpComponent: EmoneyPdpComponent

    fun getEmoneyPdpComponent(application: Application): EmoneyPdpComponent {
        if (!::emoneyPdpComponent.isInitialized) {
            val digitalCommonComponent = DaggerDigitalCommonComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
            emoneyPdpComponent = DaggerEmoneyPdpComponent.builder()
                    .digitalCommonComponent(digitalCommonComponent)
                    .build()
        }

        return emoneyPdpComponent
    }
}