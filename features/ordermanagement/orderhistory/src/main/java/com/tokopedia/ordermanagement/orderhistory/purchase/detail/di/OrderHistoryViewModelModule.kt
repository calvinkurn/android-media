package com.tokopedia.ordermanagement.orderhistory.purchase.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.presentation.viewmodel.OrderHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OrderHistoryViewModelModule {

    @Binds
    @OrderHistoryScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(OrderHistoryViewModel::class)
    internal abstract fun orderHistoryViewModel(viewModel: OrderHistoryViewModel): ViewModel
}