package com.tokopedia.sellerorder.filter.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.sellerorder.filter.presentation.viewmodel.SomFilterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SomFilterViewModelModule {

    @SomFilterScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SomFilterViewModel::class)
    internal abstract fun somFilterViewModel(viewModel: SomFilterViewModel): ViewModel
}