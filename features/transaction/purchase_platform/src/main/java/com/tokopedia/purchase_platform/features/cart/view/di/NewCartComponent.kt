package com.tokopedia.purchase_platform.features.cart.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.purchase_platform.features.cart.view.CartFragment
import com.tokopedia.purchase_platform.features.cart.view.UpdateCartIntentService
import dagger.Component

/**
 * Created by Irfan Khoirul on 2019-08-23.
 */

@CartScope
@Component(modules = [CartModule::class], dependencies = [BaseAppComponent::class])
interface NewCartComponent {
    fun inject(cartFragment: CartFragment)

    fun inject(updateCartIntentService: UpdateCartIntentService)
}