package com.tokopedia.tradein.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tradein.view.activity.TradeInHomePageActivity
import com.tokopedia.tradein.view.activity.TradeInInfoActivity
import com.tokopedia.tradein.view.bottomsheet.TradeInImeiBS
import com.tokopedia.tradein.view.fragment.TradeInHomePageFragment
import com.tokopedia.tradein.view.fragment.TradeInPromoDetailPageFragment
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