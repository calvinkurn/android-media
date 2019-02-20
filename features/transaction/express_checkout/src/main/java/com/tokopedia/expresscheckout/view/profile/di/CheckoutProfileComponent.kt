package com.tokopedia.expresscheckout.view.profile.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.expresscheckout.view.profile.CheckoutProfileBottomSheet
import dagger.Component

/**
 * Created by Irfan Khoirul on 03/02/19.
 */

@CheckoutProfileScope
@Component(modules = [CheckoutProfileModule::class], dependencies = [BaseAppComponent::class])
interface CheckoutProfileComponent {
    fun inject(bottomsheet: CheckoutProfileBottomSheet)
}