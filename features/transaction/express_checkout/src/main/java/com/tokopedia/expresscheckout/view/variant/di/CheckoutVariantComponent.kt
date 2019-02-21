package com.tokopedia.expresscheckout.view.variant.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantFragment
import dagger.Component

/**
 * Created by Irfan Khoirul on 03/02/19.
 */

@CheckoutVariantScope
@Component(modules = [CheckoutVariantModule::class], dependencies = [BaseAppComponent::class])
interface CheckoutVariantComponent {
    fun inject(checkoutVariantFragment: CheckoutVariantFragment)
}