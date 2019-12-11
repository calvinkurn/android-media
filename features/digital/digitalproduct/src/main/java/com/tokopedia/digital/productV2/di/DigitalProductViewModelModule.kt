package com.tokopedia.digital.productV2.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.digital.productV2.presentation.viewmodel.DigitalProductViewModel
import com.tokopedia.digital.productV2.presentation.viewmodel.SharedDigitalProductViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@DigitalProductScope
abstract class DigitalProductViewModelModule {


    @DigitalProductScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DigitalProductViewModel::class)
    internal abstract fun digitalProductViewModel(viewModel: DigitalProductViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SharedDigitalProductViewModel::class)
    internal abstract fun sharedDigitalProductViewModel(viewModel: SharedDigitalProductViewModel): ViewModel
}