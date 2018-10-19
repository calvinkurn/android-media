package com.tokopedia.promocheckout.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.promocheckout.list.PromoCheckoutListFragment
import dagger.Component

@PromoCheckoutDetailScope
@Component(modules = arrayOf(PromoCheckoutDetailModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface PromoCheckoutDetailComponent{
    fun inject(promoCheckoutListFragment: PromoCheckoutListFragment)
}