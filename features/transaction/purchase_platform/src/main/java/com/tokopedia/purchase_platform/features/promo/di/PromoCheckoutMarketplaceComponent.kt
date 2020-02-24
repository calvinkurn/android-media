package com.tokopedia.purchase_platform.features.promo.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.purchase_platform.features.promo.presentation.fragment.PromoCheckoutMarketplaceFragment
import dagger.Component

@PromoCheckoutMarketplaceScope
@Component(modules = [PromoCheckoutMarketplaceModule::class], dependencies = [BaseAppComponent::class])
interface PromoCheckoutMarketplaceComponent {
    fun inject(promoCheckoutMarketplaceFragment: PromoCheckoutMarketplaceFragment)
}