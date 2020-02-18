package com.tokopedia.topupbills.telco.view.di

import android.app.Application
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance

/**
 * Created by nabillasabbaha on 07/05/19.
 */
object DigitalTopupInstance {

    fun getComponent(application: Application): DigitalTopupComponent {
        val digitalTopupComponent: DigitalTopupComponent by lazy {
            DaggerDigitalTopupComponent.builder()
                    .commonTopupBillsComponent(CommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
                    .build()
        }
        return digitalTopupComponent
    }
}