package com.tokopedia.promocheckout.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.promocheckout.detail.view.fragment.CheckoutCatalogDetailFragment
import com.tokopedia.promocheckout.detail.view.fragment.PromoCheckoutDetailDigitalFragment
import com.tokopedia.promocheckout.detail.view.fragment.PromoCheckoutDetailMarketplaceFragment
import com.tokopedia.promocheckout.list.view.fragment.PromoCheckoutListMarketplaceFragment
import dagger.Component

@PromoCheckoutDetailScope
@Component(modules = arrayOf(PromoCheckoutDetailModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface PromoCheckoutDetailComponent{
    fun inject(promoCheckoutDetailMarketplaceFragment: PromoCheckoutDetailMarketplaceFragment)
    fun inject(promoCheckoutDetailDigitalFragment: PromoCheckoutDetailDigitalFragment)
    fun inject(checkoutDetailDigitalFragment: CheckoutCatalogDetailFragment)
}