package com.tokopedia.promocheckoutmarketplace.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.promocheckoutmarketplace.presentation.PromoCheckoutFragment
import dagger.Component

@PromoCheckoutMarketplaceScope
@Component(modules = [PromoCheckoutViewModelModule::class, PromoCheckoutModule::class], dependencies = [BaseAppComponent::class])
interface PromoCheckoutMarketplaceComponent {
    fun inject(promoCheckoutFragment: PromoCheckoutFragment)
}