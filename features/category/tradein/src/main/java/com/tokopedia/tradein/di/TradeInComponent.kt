package com.tokopedia.tradein.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tradein.view.viewcontrollers.activity.*
import com.tokopedia.tradein.view.viewcontrollers.bottomsheet.TradeInImeiBS
import com.tokopedia.tradein.view.viewcontrollers.fragment.*
import dagger.Component

@TradeInScope
@Component(modules = [TradeInUseCaseModule::class,
    TradeInViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface TradeInComponent {
    fun inject(tradeInHomeActivity: TradeInHomeActivity)
    fun inject(tradeInAddressFragment: TradeInAddressFragment)
    fun inject(tradeInInitialPriceFragment: TradeInInitialPriceFragment)
    fun inject(tradeInFinalPriceFragment: TradeInFinalPriceFragment)
    fun inject(tradeInInfoActivity: TradeInInfoActivity)
    fun inject(tradeInPromoDetailPageFragment: TradeInPromoDetailPageFragment)
    fun inject(tradeInHomePageActivity: TradeInHomePageActivity)
    fun inject(tradeInHomePageFragment: TradeInHomePageFragment)
    fun inject(tradeInImeiBS: TradeInImeiBS)
}