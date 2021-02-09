package com.tokopedia.emoney.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.emoney.viewmodel.EmoneyBalanceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DigitalEmoneyViewModelModule {

    @DigitalEmoneyScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(EmoneyBalanceViewModel::class)
    internal abstract fun emoneyBalanceViewModel(customViewModel: EmoneyBalanceViewModel): ViewModel
}