package com.tokopedia.moneyin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import com.tokopedia.moneyin.viewmodel.MoneyInCheckoutViewModel
import com.tokopedia.moneyin.viewmodel.FinalPriceViewModel
import com.tokopedia.moneyin.viewmodel.MoneyInHomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MoneyInViewModelModule {

    @MoneyInScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory) : ViewModelProvider.Factory

    @Binds
    @IntoMap
    @MoneyInScope
    @ViewModelKey(MoneyInHomeViewModel::class)
    internal abstract fun tradeInHomeViewModel(viewModel: MoneyInHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @MoneyInScope
    @ViewModelKey(FinalPriceViewModel::class)
    internal abstract fun finalPriceViewModel(viewModel: FinalPriceViewModel): ViewModel

    @Binds
    @IntoMap
    @MoneyInScope
    @ViewModelKey(MoneyInCheckoutViewModel::class)
    internal abstract fun moneyInCheckoutViewModel(viewModel: MoneyInCheckoutViewModel): ViewModel

}