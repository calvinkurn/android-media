package com.tokopedia.sellerorder.orderextension.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.sellerorder.orderextension.presentation.fragment.SomOrderExtensionRequestFragment
import dagger.Component

@SomOrderExtensionRequestScope
@Component(
    modules = [SomOrderExtensionModule::class, SomOrderExtensionViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface SomOrderExtensionRequestComponent {
    fun inject(somOrderExtensionRequestFragment: SomOrderExtensionRequestFragment)
}
