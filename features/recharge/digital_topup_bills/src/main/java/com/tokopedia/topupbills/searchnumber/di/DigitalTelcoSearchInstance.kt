package com.tokopedia.topupbills.searchnumber.di

import android.app.Application
import com.tokopedia.topupbills.common.di.DigitalTopupInstance

object DigitalTelcoSearchInstance {

    fun getComponent(application: Application): DigitalTelcoSearchComponent {
        val digitalTelcoSearchComponent: DigitalTelcoSearchComponent by lazy {
            DaggerDigitalTelcoSearchComponent.builder()
                    .digitalTopupComponent(DigitalTopupInstance.getDigitalTopupComponent(application))
                    .build()
        }
        return digitalTelcoSearchComponent
    }
}