package com.tokopedia.statistic.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.statistic.di.StatisticScope
import com.tokopedia.statistic.view.viewmodel.StatisticActivityViewModel
import com.tokopedia.statistic.view.viewmodel.StatisticViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By @ilhamsuaib on 09/06/20
 */

@Module
abstract class StatisticViewModelModule {

    @StatisticScope
    @Binds
    abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(StatisticViewModel::class)
    abstract fun provideStatisticViewModel(viewModel: StatisticViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatisticActivityViewModel::class)
    abstract fun provideStatisticActivityViewModel(viewModel: StatisticActivityViewModel): ViewModel
}