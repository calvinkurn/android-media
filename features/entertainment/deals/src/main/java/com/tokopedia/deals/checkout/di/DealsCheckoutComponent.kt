package com.tokopedia.deals.checkout.di

import com.tokopedia.deals.checkout.ui.fragment.DealsCheckoutFragment
import com.tokopedia.deals.common.di.DealsComponent
import dagger.Component

@DealsCheckoutScope
@Component(
    modules = [
        DealsCheckoutViewModelModule::class
    ],
    dependencies = [
        DealsComponent::class
    ]
)
interface DealsCheckoutComponent {
    fun inject(dealsCheckoutFragment: DealsCheckoutFragment)
}
