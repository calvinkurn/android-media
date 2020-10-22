package com.tokopedia.topupbills.telco.common.di

import android.app.Application
import com.tokopedia.topupbills.common.di.DigitalTopupInstance

/**
 * Created by nabillasabbaha on 07/05/19.
 */
object DigitalTelcoInstance {

    fun getComponent(application: Application): DigitalTelcoComponent {
        val digitalTelcoComponent: DigitalTelcoComponent by lazy {
            DaggerDigitalTelcoComponent.builder()
                    .digitalTopupComponent(DigitalTopupInstance.getDigitalTopupComponent(application))
                    .build()
        }
        return digitalTelcoComponent
    }
}