package com.tokopedia.promocheckout.list.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailModule
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailScope
import com.tokopedia.promocheckout.list.PromoCheckoutListFragment
import dagger.Component

@PromoCheckoutListScope
@Component(modules = arrayOf(PromoCheckoutListModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface PromoCheckoutListComponent{
    fun inject(promoCheckoutListFragment: PromoCheckoutListFragment)
}