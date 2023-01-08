package com.tokopedia.buyerorder.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.buyerorder.detail.revamp.viewModel.OrderDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * created by @bayazidnasir on 19/8/2022
 */

@Module
abstract class OrderDetailViewModelModule {

    @Binds
    @OrderDetailScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(OrderDetailViewModel::class)
    internal abstract fun bindOrderDetailViewModel(orderDetailViewModel: OrderDetailViewModel): ViewModel
}