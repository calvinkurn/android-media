package com.tokopedia.purchase_platform.features.one_click_checkout.order.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@OrderSummaryPageScope
@Module
abstract class OrderSummaryPageViewModelModule {

    @OrderSummaryPageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactor: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(OrderSummaryPageViewModel::class)
    internal abstract fun provideOrderSummaryPageViewModel(viewModel: OrderSummaryPageViewModel): ViewModel
}