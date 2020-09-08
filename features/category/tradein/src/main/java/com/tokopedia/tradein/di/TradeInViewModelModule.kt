package com.tokopedia.tradein.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import com.tokopedia.tradein.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@TradeInScope
abstract class TradeInViewModelModule {

    @TradeInScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(MoneyInHomeViewModel::class)
    internal abstract fun moneyInHomeViewModel(viewModel: MoneyInHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(FinalPriceViewModel::class)
    internal abstract fun finalPriceViewModel(viewModel: FinalPriceViewModel): ViewModel

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(MoneyInCheckoutViewModel::class)
    internal abstract fun moneyInCheckoutViewModel(viewModel: MoneyInCheckoutViewModel): ViewModel

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(TradeInHomeViewModel::class)
    internal abstract fun tradeInHomeViewModel(viewModel: TradeInHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(TradeInAddressViewModel::class)
    internal abstract fun tradeInAddressViewModel(viewModel: TradeInAddressViewModel): ViewModel

}