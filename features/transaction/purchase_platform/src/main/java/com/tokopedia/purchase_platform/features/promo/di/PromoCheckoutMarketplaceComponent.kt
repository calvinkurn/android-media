package com.tokopedia.purchase_platform.features.promo.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.purchase_platform.features.promo.presentation.PromoCheckoutFragment
import com.tokopedia.sellerorder.common.di.PromoCheckoutModule
import dagger.Component

@PromoCheckoutMarketplaceScope
@Component(modules = [PromoCheckoutViewModelModule::class, PromoCheckoutModule::class], dependencies = [BaseAppComponent::class])
interface PromoCheckoutMarketplaceComponent {
//    @ApplicationContext
//    fun context(): Context
//
//    fun dispatcher(): CoroutineDispatcher
//
//    fun graphQlRepository(): GraphqlRepository

    fun inject(promoCheckoutFragment: PromoCheckoutFragment)
}