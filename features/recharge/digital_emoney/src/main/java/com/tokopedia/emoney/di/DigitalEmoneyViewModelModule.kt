package com.tokopedia.emoney.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.emoney.viewmodel.BrizziViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(BrizziViewModel::class)
    internal abstract fun brizziTokenViewModel(customViewModel: BrizziViewModel): ViewModel

}