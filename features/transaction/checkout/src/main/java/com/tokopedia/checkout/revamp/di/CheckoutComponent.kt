package com.tokopedia.checkout.revamp.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.checkout.revamp.view.CheckoutFragment
import dagger.Component

@ActivityScope
@Component(modules = [CheckoutModule::class, CheckoutViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CheckoutComponent {

    fun inject(checkoutFragment: CheckoutFragment)
}
