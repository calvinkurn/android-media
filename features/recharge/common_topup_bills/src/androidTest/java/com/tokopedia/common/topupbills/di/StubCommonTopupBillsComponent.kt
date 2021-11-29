package com.tokopedia.common.topupbills.di

import com.tokopedia.common_digital.common.di.DigitalCommonComponent
import dagger.Component

@CommonTopupBillsScope
@Component(
    modules = [
        StubCommonTopupBillsModule::class,
        TopupBillsViewModelModule::class
    ],
    dependencies = [
        DigitalCommonComponent::class
    ]
)
interface StubCommonTopupBillsComponent: CommonTopupBillsComponent {

}