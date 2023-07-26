package com.tokopedia.buy_more_get_more.di.module

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.buy_more_get_more.di.scope.BuyMoreGetMoreScope
import dagger.Binds

@dagger.Module
abstract class BuyMoreGetMoreViewModelModule {

    @BuyMoreGetMoreScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
