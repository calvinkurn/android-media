package com.tokopedia.topupbills.telco.view.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common_digital.common.di.DaggerDigitalCommonComponent
import com.tokopedia.common_digital.common.di.DigitalCommonComponent

/**
 * Created by nabillasabbaha on 07/05/19.
 */
object DigitalTopupInstance {

    fun getComponent(application: Application): DigitalTopupComponent {
        val digitalTopupComponent: DigitalTopupComponent by lazy {
            val digitalCommonComponent = DaggerDigitalCommonComponent.builder()
                        .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                        .build()
            DaggerDigitalTopupComponent.builder()
                    .digitalCommonComponent(digitalCommonComponent)
                    .build()
        }
        return digitalTopupComponent
    }
}