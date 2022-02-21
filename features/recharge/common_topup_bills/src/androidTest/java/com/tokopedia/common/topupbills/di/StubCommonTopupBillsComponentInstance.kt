package com.tokopedia.common.topupbills.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common_digital.common.di.DaggerDigitalCommonComponent


object StubCommonTopupBillsComponentInstance {

    fun getCommonTopupBillsComponent(application: Application): CommonTopupBillsComponent {
        val commonTopupBillsComponent: CommonTopupBillsComponent by lazy {
            val digitalCommonComponent = DaggerDigitalCommonComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
            DaggerStubCommonTopupBillsComponent.builder()
                .digitalCommonComponent(digitalCommonComponent)
                .build()
        }
        return commonTopupBillsComponent
    }
}