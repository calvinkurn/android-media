package com.tokopedia.topads.dashboard.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topads.credit.history.view.viewmodel.TopAdsCreditHistoryViewModel
import com.tokopedia.topads.dashboard.view.model.GroupDetailViewModel
import com.tokopedia.topads.dashboard.viewmodel.TopAdsDashboardViewModel
import com.tokopedia.topads.dashboard.viewmodel.TopAdsEducationViewModel
import com.tokopedia.topads.dashboard.viewmodel.TopAdsInsightViewModel
import com.tokopedia.topads.debit.autotopup.view.viewmodel.TopAdsAutoTopUpViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @TopAdsDashboardScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsCreditHistoryViewModel::class)
    internal abstract fun topadsCreditHistoryViewModel(viewModel: TopAdsCreditHistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsAutoTopUpViewModel::class)
    internal abstract fun topadsAutoTopUpViewModel(viewModel: TopAdsAutoTopUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GroupDetailViewModel::class)
    internal abstract fun topadsGroupDetailViewModel(viewModel: GroupDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsInsightViewModel::class)
    internal abstract fun bindsTopAdsInsightViewModel(viewModel: TopAdsInsightViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsDashboardViewModel::class)
    internal abstract fun bindsTopAdsDashboardViewModel(viewModel: TopAdsDashboardViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsEducationViewModel::class)
    internal abstract fun bindsTopAdsEducationViewModel(viewModel: TopAdsEducationViewModel): ViewModel
}