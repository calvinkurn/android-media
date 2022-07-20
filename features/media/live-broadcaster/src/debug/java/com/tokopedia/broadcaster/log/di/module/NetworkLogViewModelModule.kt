package com.tokopedia.broadcaster.log.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.broadcaster.log.di.scope.NetworkLogScope
import com.tokopedia.broadcaster.log.ui.viewmodel.NetworkLogViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module abstract class NetworkLogViewModelModule {

    @Binds
    @NetworkLogScope
    internal abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @NetworkLogScope
    @ViewModelKey(NetworkLogViewModel::class)
    internal abstract fun provideNetworkChuckerViewModel(
        viewModel: NetworkLogViewModel
    ): ViewModel

}