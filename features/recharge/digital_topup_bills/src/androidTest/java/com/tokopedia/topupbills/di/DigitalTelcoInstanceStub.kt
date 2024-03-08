package com.tokopedia.topupbills.di

import android.app.Application
import com.tokopedia.topupbills.common.di.DigitalTopupInstance
import com.tokopedia.topupbills.telco.common.di.DigitalTelcoComponent

/**
 * Created by nabillasabbaha on 07/05/19.
 */
object DigitalTelcoInstanceStub {

    fun getComponent(application: Application): DigitalTelcoComponent {
        val digitalTelcoComponent: DigitalTelcoComponentStub by lazy {
            DaggerDigitalTelcoComponentStub.builder()
                    .digitalTopupComponent(DigitalTopupInstance.getDigitalTopupComponent(application))
                    .build()
        }
        return digitalTelcoComponent
    }
}
