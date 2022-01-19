package com.tokopedia.cart.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.cart.view.CartFragment
import dagger.Component

/**
 * Created by Irfan Khoirul on 2019-08-23.
 */

@CartScope
@Component(modules = [CartModule::class], dependencies = [BaseAppComponent::class])
interface CartComponent {
    fun inject(cartFragment: CartFragment)
}