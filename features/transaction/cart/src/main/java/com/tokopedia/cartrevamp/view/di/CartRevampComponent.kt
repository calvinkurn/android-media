package com.tokopedia.cartrevamp.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.cartrevamp.view.CartRevampFragment
import dagger.Component

@CartRevampScope
@Component(modules = [CartRevampModule::class, CartViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CartRevampComponent {
    fun inject(cartFragment: CartRevampFragment)
}
