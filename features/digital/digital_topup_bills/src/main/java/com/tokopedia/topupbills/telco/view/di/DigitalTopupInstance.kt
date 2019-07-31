package com.tokopedia.topupbills.telco.view.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

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