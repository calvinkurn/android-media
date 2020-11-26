package com.tokopedia.home.account.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.home.account.di.scope.BuyerAccountScope
import com.tokopedia.home.account.revamp.viewmodel.BuyerAccountViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BuyerAccountViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(BuyerAccountViewModel::class)
    abstract fun buyerAccountViewModel(viewModel: BuyerAccountViewModel): ViewModel

    @Binds
    @BuyerAccountScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}