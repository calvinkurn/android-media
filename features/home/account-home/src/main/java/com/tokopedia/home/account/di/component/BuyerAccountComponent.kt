package com.tokopedia.home.account.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home.account.di.module.BuyerAccountModule
import com.tokopedia.home.account.di.module.BuyerAccountViewModelModule
import com.tokopedia.home.account.di.module.RevampedBuyerAccountModule
import com.tokopedia.home.account.di.scope.BuyerAccountScope
import com.tokopedia.home.account.presentation.fragment.BuyerAccountFragment
import com.tokopedia.recommendation_widget_common.di.RecommendationModule

import dagger.Component

/**
 * @author okasurya on 7/17/18.
 */
@Component(modules = [BuyerAccountModule::class,
    BuyerAccountViewModelModule::class,
    RecommendationModule::class,
    RevampedBuyerAccountModule::class], dependencies = [BaseAppComponent::class])
@BuyerAccountScope
interface BuyerAccountComponent {
    fun inject(fragment: BuyerAccountFragment)
}
