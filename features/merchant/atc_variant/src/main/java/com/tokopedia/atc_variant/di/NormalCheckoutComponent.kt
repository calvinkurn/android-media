package com.tokopedia.atc_variant.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.atc_variant.view.NormalChekoutFragment
import dagger.Component

@NormalCheckoutScope
@Component(modules = [
    NormalCheckoutModule::class,
    ViewModelModule::class,
    GqlRawQueryModule::class],
        dependencies = [BaseAppComponent::class])
interface NormalCheckoutComponent {
    fun inject(fragment: NormalChekoutFragment)
}