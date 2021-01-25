package com.tokopedia.orderhistory.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.orderhistory.view.viewmodel.OrderHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OrderHistoryViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(OrderHistoryViewModel::class)
    abstract fun provideOrderHistoryViewModel(viewModel: OrderHistoryViewModel): ViewModel

    @Binds
    @OrderHistoryScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}