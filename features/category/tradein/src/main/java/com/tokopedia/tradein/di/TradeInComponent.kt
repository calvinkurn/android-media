package com.tokopedia.tradein.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tradein.view.viewcontrollers.activity.FinalPriceActivity
import com.tokopedia.tradein.view.viewcontrollers.activity.MoneyInCheckoutActivity
import com.tokopedia.tradein.view.viewcontrollers.activity.MoneyInHomeActivity
import com.tokopedia.tradein.view.viewcontrollers.activity.TradeInHomeActivity
import dagger.Component

@TradeInScope
@Component(modules = [TradeInUseCaseModule::class,
    TradeInViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface TradeInComponent {
    fun inject(tradeInHomeActivity: TradeInHomeActivity)
    fun inject(moneyInHomeActivity: MoneyInHomeActivity)
    fun inject(finalPriceActivity: FinalPriceActivity)
    fun inject(moneyInCheckoutActivity: MoneyInCheckoutActivity)
}