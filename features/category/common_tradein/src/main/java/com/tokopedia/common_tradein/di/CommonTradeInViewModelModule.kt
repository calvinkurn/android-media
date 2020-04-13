package com.tokopedia.common_tradein.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import com.tokopedia.common_tradein.viewmodel.TradeInTextViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
@CommonTradeInScope
abstract class CommonTradeInViewModelModule {

    @CommonTradeInScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory) : ViewModelProvider.Factory


    @Binds
    @IntoMap
    @CommonTradeInScope
    @ViewModelKey(TradeInTextViewModel::class)
    internal abstract fun tradeInTextViewModel(viewModel: TradeInTextViewModel): ViewModel

}