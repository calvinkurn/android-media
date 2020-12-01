package com.tokopedia.tradein.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tradein.view.viewcontrollers.activity.*
import com.tokopedia.tradein.view.viewcontrollers.fragment.TradeInAddressFragment
import com.tokopedia.tradein.view.viewcontrollers.fragment.TradeInFinalPriceFragment
import com.tokopedia.tradein.view.viewcontrollers.fragment.TradeInInitialPriceFragment
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
    fun inject(tradeInAddressFragment: TradeInAddressFragment)
    fun inject(tradeInInitialPriceFragment: TradeInInitialPriceFragment)
    fun inject(tradeInFinalPriceFragment: TradeInFinalPriceFragment)
    fun inject(tradeInInfoActivity: TradeInInfoActivity)
}