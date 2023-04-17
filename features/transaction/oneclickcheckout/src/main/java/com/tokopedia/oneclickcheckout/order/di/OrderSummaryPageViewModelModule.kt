package com.tokopedia.oneclickcheckout.order.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OrderSummaryPageViewModelModule {

    @OrderSummaryPageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactor: ViewModelFactory): ViewModelProvider.Factory

    @OrderSummaryPageScope
    @Binds
    @IntoMap
    @ViewModelKey(OrderSummaryPageViewModel::class)
    internal abstract fun provideOrderSummaryPageViewModel(viewModel: OrderSummaryPageViewModel): ViewModel
}
