package com.tokopedia.sellerorder.orderextension.di

import com.tokopedia.sellerorder.common.di.SomComponent
import com.tokopedia.sellerorder.orderextension.presentation.fragment.SomOrderExtensionRequestFragment
import dagger.Component

@SomOrderExtensionRequestScope
@Component(
    modules = [SomOrderExtensionViewModelModule::class],
    dependencies = [SomComponent::class]
)
interface SomOrderExtensionRequestComponent {
    fun inject(somOrderExtensionRequestFragment: SomOrderExtensionRequestFragment)
}
