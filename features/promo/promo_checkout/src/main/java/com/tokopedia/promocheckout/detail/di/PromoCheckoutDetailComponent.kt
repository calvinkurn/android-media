package com.tokopedia.promocheckout.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.promocheckout.detail.view.fragment.*
import com.tokopedia.user.session.UserSession
import dagger.Component

@PromoCheckoutDetailScope
@Component(modules = arrayOf(PromoCheckoutDetailModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface PromoCheckoutDetailComponent{
    fun inject(promoCheckoutDetailMarketplaceFragment: PromoCheckoutDetailMarketplaceFragment)
    fun inject(promoCheckoutDetailDigitalFragment: PromoCheckoutDetailDigitalFragment)
    fun inject(checkoutDetailDigitalFragment: CheckoutCatalogDetailFragment)
    fun inject(promoCheckoutDetailFlightFragment: PromoCheckoutDetailFlightFragment)
    fun inject(promoCheckoutDetailHotelFragment: PromoCheckoutDetailHotelFragment)
    fun inject(promoCheckoutDetailEventFragment: PromoCheckoutDetailEventFragment)
    fun inject(promoCheckoutDetailDealsFragment: PromoCheckoutDetailDealsFragment)

    fun userSession(): UserSession
}