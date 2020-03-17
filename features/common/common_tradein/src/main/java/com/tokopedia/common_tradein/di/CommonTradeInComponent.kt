package com.tokopedia.common_tradein.di

import com.tokopedia.common_tradein.customviews.TradeInTextView
import dagger.Component

@CommonTradeInScope
@Component(modules = [CommonTradeInViewModelModule::class])
interface CommonTradeInComponent{
    fun inject(tradeInTextView: TradeInTextView)
}