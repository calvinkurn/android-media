package com.tokopedia.emoney.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.emoney.viewmodel.EmoneyInquiryBalanceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@DigitalEmoneyScope
abstract class DigitalEmoneyViewModelModule {

    @DigitalEmoneyScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(EmoneyInquiryBalanceViewModel::class)
    internal abstract fun emoneyInquiryBalanceViewModel(customViewModel: EmoneyInquiryBalanceViewModel): ViewModel

}