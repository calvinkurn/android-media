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
abstract class TradeInViewModelModule {

    @TradeInScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(TradeInHomeViewModel::class)
    internal abstract fun tradeInHomeViewModel(viewModel: TradeInHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(FinalPriceViewModel::class)
    internal abstract fun finalPriceViewModel(viewModel: FinalPriceViewModel): ViewModel

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(TradeInAddressViewModel::class)
    internal abstract fun tradeInAddressViewModel(viewModel: TradeInAddressViewModel): ViewModel

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(TradeInInitialPriceViewModel::class)
    internal abstract fun tradeInInitialPriceViewModel(viewModel: TradeInInitialPriceViewModel): ViewModel

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(TradeInInfoViewModel::class)
    internal abstract fun tradeInInfoViewModel(viewModel: TradeInInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(TradeInHomePageVM::class)
    internal abstract fun tradeInHomePageVM(viewModel: TradeInHomePageVM): ViewModel

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(TradeInPromoDetailPageVM::class)
    internal abstract fun tradeInPromoDetailPageVM(viewModel: TradeInPromoDetailPageVM): ViewModel

    @Binds
    @IntoMap
    @TradeInScope
    @ViewModelKey(TradeInHomePageFragmentVM::class)
    internal abstract fun tradeInHomePageFragmentVM(viewModel: TradeInHomePageFragmentVM): ViewModel

}