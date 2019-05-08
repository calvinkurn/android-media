package com.tokopedia.topupbills.telco.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.topupbills.di.DaggerDigitalTopupComponent

/**
 * Created by nabillasabbaha on 07/05/19.
 */
object DigitalTopupInstance {

    fun getComponent(application: Application): DigitalTopupComponent {
        val digitalTopupComponent: DigitalTopupComponent by lazy {
            DaggerDigitalTopupComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()
        }
        return digitalTopupComponent
    }
}