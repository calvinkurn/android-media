package com.tokopedia.buy_more_get_more.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.buy_more_get_more.di.module.BuyMoreGetMoreModule
import com.tokopedia.buy_more_get_more.di.module.BuyMoreGetMoreViewModelModule
import com.tokopedia.buy_more_get_more.di.scope.BuyMoreGetMoreScope
import com.tokopedia.buy_more_get_more.presentation.olp.OfferLandingPageActivity
import com.tokopedia.buy_more_get_more.presentation.olp.OfferLandingPageFragment
import dagger.Component

@BuyMoreGetMoreScope
@Component(
    modules = [BuyMoreGetMoreModule::class, BuyMoreGetMoreViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface BuyMoreGetMoreComponent {

    fun inject(activity: OfferLandingPageActivity)

    fun inject(fragment: OfferLandingPageFragment)
}
