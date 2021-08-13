package com.tokopedia.sellerorder.waitingpaymentorder.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.viewmodel.WaitingPaymentOrderViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

@Module
abstract class WaitingPaymentOrderViewModelModule {

    @WaitingPaymentOrderScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(WaitingPaymentOrderViewModel::class)
    internal abstract fun waitingPaymentOrderViewModel(viewModel: WaitingPaymentOrderViewModel): ViewModel
}