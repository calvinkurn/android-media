package com.tokopedia.promocheckout.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.promocheckout.list.view.fragment.*
import dagger.Component

@PromoCheckoutListScope
@Component(modules = arrayOf(PromoCheckoutListModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface PromoCheckoutListComponent{
    fun inject(basePromoCheckoutListFragment: BasePromoCheckoutListFragment)
    fun inject(promoCheckoutListMarketplaceFragment: PromoCheckoutListMarketplaceFragment)
    fun inject(promoCheckoutListDigitalFragment: PromoCheckoutListDigitalFragment)
    fun inject(promoCheckoutListFlightFragment: PromoCheckoutListFlightFragment)
    fun inject(promoCheckoutListHotelFragment: PromoCheckoutListHotelFragment)
}