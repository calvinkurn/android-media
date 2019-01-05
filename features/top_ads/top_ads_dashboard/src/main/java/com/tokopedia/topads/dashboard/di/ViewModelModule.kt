package com.tokopedia.topads.dashboard.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.topads.common.view.TopAdsDatePickerViewModel
import com.tokopedia.topads.dashboard.view.viewmodel.TopAdsCreditHistoryViewModel
import com.tokopedia.topads.dashboard.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@TopAdsDashboardScope
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsCreditHistoryViewModel::class)
    internal abstract fun topadsCreditHistoryViewModel(viewModel: TopAdsCreditHistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsDatePickerViewModel::class)
    internal abstract fun topadsDatePickerViewModel(viewModel: TopAdsDatePickerViewModel): ViewModel
}