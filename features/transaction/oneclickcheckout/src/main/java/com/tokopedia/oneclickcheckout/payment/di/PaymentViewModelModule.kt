package com.tokopedia.oneclickcheckout.payment.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.oneclickcheckout.payment.list.view.PaymentListingViewModel
import com.tokopedia.oneclickcheckout.payment.topup.view.OvoTopUpWebViewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PaymentViewModelModule {

    @PaymentScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactor: ViewModelFactory): ViewModelProvider.Factory

    @PaymentScope
    @Binds
    @IntoMap
    @ViewModelKey(PaymentListingViewModel::class)
    internal abstract fun providePaymentListingViewModel(viewModel: PaymentListingViewModel): ViewModel

    @PaymentScope
    @Binds
    @IntoMap
    @ViewModelKey(OvoTopUpWebViewViewModel::class)
    internal abstract fun provideOvoTopUpWebViewViewModel(viewModel: OvoTopUpWebViewViewModel): ViewModel
}