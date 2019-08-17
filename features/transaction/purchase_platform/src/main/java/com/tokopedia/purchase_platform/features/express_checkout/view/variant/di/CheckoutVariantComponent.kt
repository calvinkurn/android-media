package com.tokopedia.purchase_platform.features.express_checkout.view.variant.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.purchase_platform.common.di.component.CartComponent
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.CheckoutVariantFragment
import dagger.Component

/**
 * Created by Irfan Khoirul on 03/02/19.
 */

@CheckoutVariantScope
@Component(modules = [CheckoutVariantModule::class], dependencies = [CartComponent::class])
interface CheckoutVariantComponent {
    fun inject(checkoutVariantFragment: CheckoutVariantFragment)
}