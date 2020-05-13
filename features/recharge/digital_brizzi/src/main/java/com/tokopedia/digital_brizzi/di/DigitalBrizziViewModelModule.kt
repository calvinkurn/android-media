package com.tokopedia.digital_brizzi.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.digital_brizzi.viewmodel.BrizziBalanceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@DigitalBrizziScope
abstract class DigitalBrizziViewModelModule {

    @DigitalBrizziScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(BrizziBalanceViewModel::class)
    internal abstract fun brizziBalanceViewModel(customBalanceViewModel: BrizziBalanceViewModel): ViewModel

}