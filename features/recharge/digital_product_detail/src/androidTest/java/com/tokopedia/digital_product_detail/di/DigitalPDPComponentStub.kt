package com.tokopedia.digital_product_detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

@DigitalPDPScope
@Component(
    modules = [
        DigitalPDPModuleStub::class,
        DigitalPDPViewModelModule::class,
        DigitalPDPBindModule::class
    ],
    dependencies = [
        BaseAppComponent::class
    ]
)
interface DigitalPDPComponentStub: DigitalPDPComponent {

}
