package com.tokopedia.pms.howtopay_native.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.pms.howtopay_native.ui.fragment.HowToPayFragment
import dagger.Component

@HowToPayScope
@Component(modules = [ViewModelModule::class, HowToPayModule::class],
        dependencies = [BaseAppComponent::class])
interface HowToPayComponent {
    fun inject(howToPayFragment: HowToPayFragment)
}