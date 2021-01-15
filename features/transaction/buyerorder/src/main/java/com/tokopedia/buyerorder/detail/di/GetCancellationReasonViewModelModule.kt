package com.tokopedia.buyerorder.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.buyerorder.detail.view.viewmodel.BuyerCancellationViewModel
import com.tokopedia.buyerorder.list.di.OrderListModuleScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by fwidjaja on 12/06/20.
 */

@Module
abstract class GetCancellationReasonViewModelModule {
    @OrderListModuleScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @OrderListModuleScope
    @Binds
    @IntoMap
    @ViewModelKey(BuyerCancellationViewModel::class)
    internal abstract fun buyerGetCancellationReasonViewModel(viewModel: BuyerCancellationViewModel): ViewModel
}