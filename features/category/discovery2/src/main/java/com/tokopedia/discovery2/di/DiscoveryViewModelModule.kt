package com.tokopedia.discovery2.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class DiscoveryViewModelModule {

    @Binds
    @DiscoveryScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @DiscoveryScope
    @ViewModelKey(DiscoveryViewModel::class)
    internal abstract fun discoveryViewModel(viewModel: DiscoveryViewModel): ViewModel

}