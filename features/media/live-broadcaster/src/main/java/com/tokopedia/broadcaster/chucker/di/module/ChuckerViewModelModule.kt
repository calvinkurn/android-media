package com.tokopedia.broadcaster.chucker.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.broadcaster.chucker.di.scope.ChuckerScope
import com.tokopedia.broadcaster.chucker.ui.viewmodel.NetworkChuckerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module abstract class ChuckerViewModelModule {

    @Binds
    @ChuckerScope
    internal abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ChuckerScope
    @ViewModelKey(NetworkChuckerViewModel::class)
    internal abstract fun provideNetworkChuckerViewModel(
        viewModel: NetworkChuckerViewModel
    ): ViewModel

}