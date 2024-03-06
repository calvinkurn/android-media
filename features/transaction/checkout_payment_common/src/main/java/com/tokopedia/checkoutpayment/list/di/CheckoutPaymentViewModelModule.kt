package com.tokopedia.checkoutpayment.list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.checkoutpayment.list.view.PaymentListingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CheckoutPaymentViewModelModule {

    @CheckoutPaymentScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactor: ViewModelFactory): ViewModelProvider.Factory

    @CheckoutPaymentScope
    @Binds
    @IntoMap
    @ViewModelKey(PaymentListingViewModel::class)
    internal abstract fun providePaymentListingViewModel(viewModel: PaymentListingViewModel): ViewModel
}
