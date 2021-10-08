package com.tokopedia.broadcaster.statsnerd.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.broadcaster.statsnerd.di.scope.StatsNerdScope
import com.tokopedia.broadcaster.statsnerd.ui.viewmodel.NetworkStatsNerdViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module abstract class StatsNerdViewModelModule {

    @Binds
    @StatsNerdScope
    internal abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @StatsNerdScope
    @ViewModelKey(NetworkStatsNerdViewModel::class)
    internal abstract fun provideNetworkChuckerViewModel(
        viewModel: NetworkStatsNerdViewModel
    ): ViewModel

}