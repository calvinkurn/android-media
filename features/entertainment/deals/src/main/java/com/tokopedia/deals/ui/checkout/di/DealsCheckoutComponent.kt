package com.tokopedia.deals.ui.checkout.di

import com.tokopedia.deals.di.DealsComponent
import com.tokopedia.deals.ui.checkout.ui.fragment.DealsCheckoutFragment
import com.tokopedia.deals.ui.checkout.ui.fragment.DealsCheckoutLocationsFragment
import dagger.Component

@DealsCheckoutScope
@Component(
    modules = [
        DealsCheckoutModule::class,
        DealsCheckoutViewModelModule::class
    ],
    dependencies = [
        DealsComponent::class
    ]
)
interface DealsCheckoutComponent {
    fun inject(dealsCheckoutFragment: DealsCheckoutFragment)
    fun inject(dealsCheckoutLocationsFragment: DealsCheckoutLocationsFragment)
}
