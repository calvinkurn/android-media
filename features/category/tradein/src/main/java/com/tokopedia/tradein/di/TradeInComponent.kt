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
    fun inject(tradeInInfoActivity: TradeInInfoActivity)
    fun inject(tradeInHomePageActivity: TradeInHomePageActivity)
    fun inject(tradeInHomePageFragment: TradeInHomePageFragment)
    fun inject(tradeInPromoDetailPageFragment: TradeInPromoDetailPageFragment)
    fun inject(tradeInImeiBS: TradeInImeiBS)
}