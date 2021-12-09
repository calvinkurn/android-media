package com.tokopedia.dg_transaction.testing.di

import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.di.CommonTopupBillsModule
import com.tokopedia.common.topupbills.di.CommonTopupBillsScope
import com.tokopedia.common.topupbills.di.TopupBillsViewModelModule
import dagger.Component

@CommonTopupBillsScope
@Component(
    modules = [
        CommonTopupBillsModule::class,
        TopupBillsViewModelModule::class
    ],
    dependencies = [
        StubDigitalCommonComponent::class
    ]
)
interface StubCommonTopupBillsComponent: CommonTopupBillsComponent