package com.tokopedia.normalcheckout.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.normalcheckout.view.NormalCheckoutFragment
import dagger.Component

@NormalCheckoutScope
@Component(modules = [
    NormalCheckoutModule::class,
    ViewModelModule::class,
    GqlRawQueryModule::class],
        dependencies = [BaseAppComponent::class])
interface NormalCheckoutComponent {
    fun inject(fragment: NormalCheckoutFragment)
}