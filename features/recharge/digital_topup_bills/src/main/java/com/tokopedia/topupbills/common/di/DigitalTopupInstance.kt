package com.tokopedia.topupbills.common.di

import android.app.Application
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance

object DigitalTopupInstance {

    fun getDigitalTopupComponent(application: Application): DigitalTopupComponent {
        val digitalTopupComponent: DigitalTopupComponent by lazy {
            DaggerDigitalTopupComponent.builder()
                    .commonTopupBillsComponent(CommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
                    .build()
        }
        return digitalTopupComponent
    }
}