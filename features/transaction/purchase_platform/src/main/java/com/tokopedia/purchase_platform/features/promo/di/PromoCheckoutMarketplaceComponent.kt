package com.tokopedia.purchase_platform.features.promo.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.purchase_platform.features.promo.presentation.fragment.PromoCheckoutMarketplaceFragment
import com.tokopedia.sellerorder.common.di.PromoCheckoutModule
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

@PromoCheckoutMarketplaceScope
@Component(modules = [PromoCheckoutViewModelModule::class, PromoCheckoutModule::class], dependencies = [BaseAppComponent::class])
interface PromoCheckoutMarketplaceComponent {
//    @ApplicationContext
//    fun context(): Context
//
//    fun dispatcher(): CoroutineDispatcher
//
//    fun graphQlRepository(): GraphqlRepository

    fun inject(promoCheckoutMarketplaceFragment: PromoCheckoutMarketplaceFragment)
}