package com.tokopedia.promocheckout.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.promocheckout.list.BasePromoCheckoutListFragment
import com.tokopedia.promocheckout.list.PromoCheckoutListDigitalFragment
import com.tokopedia.promocheckout.list.PromoCheckoutListMarketplaceFragment
import dagger.Component

@PromoCheckoutListScope
@Component(modules = arrayOf(PromoCheckoutListModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface PromoCheckoutListComponent{
    fun inject(basePromoCheckoutListFragment: BasePromoCheckoutListFragment)
    fun inject(promoCheckoutListMarketplaceFragment: PromoCheckoutListMarketplaceFragment)
    fun inject(promoCheckoutListDigitalFragment: PromoCheckoutListDigitalFragment)
}