package com.tokopedia.oneclickcheckout.preference.edit.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.PaymentMethodViewModel
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.topup.OvoTopUpWebViewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PreferenceEditViewModelModule {

    @PreferenceEditScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactor: ViewModelFactory): ViewModelProvider.Factory

    @PreferenceEditScope
    @Binds
    @IntoMap
    @ViewModelKey(PaymentMethodViewModel::class)
    internal abstract fun providePaymentMethodViewModel(viewModel: PaymentMethodViewModel): ViewModel

    @PreferenceEditScope
    @Binds
    @IntoMap
    @ViewModelKey(OvoTopUpWebViewViewModel::class)
    internal abstract fun provideOvoTopUpWebViewViewModel(viewModel: OvoTopUpWebViewViewModel): ViewModel
}