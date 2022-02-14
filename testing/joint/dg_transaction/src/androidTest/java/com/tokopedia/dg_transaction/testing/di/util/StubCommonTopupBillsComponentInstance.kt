package com.tokopedia.dg_transaction.testing.di.util

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.dg_transaction.testing.di.DaggerStubCommonTopupBillsComponent
import com.tokopedia.dg_transaction.testing.di.DaggerStubDigitalCommonComponent

object StubCommonTopupBillsComponentInstance {

    fun getCommonTopupBillsComponent(application: Application): com.tokopedia.dg_transaction.testing.di.StubCommonTopupBillsComponent {
        val commonTopupBillsComponent: com.tokopedia.dg_transaction.testing.di.StubCommonTopupBillsComponent by lazy {
            val digitalCommonComponent = DaggerStubDigitalCommonComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
            DaggerStubCommonTopupBillsComponent.builder()
                .stubDigitalCommonComponent(digitalCommonComponent)
                .build()
        }
        return commonTopupBillsComponent
    }
}