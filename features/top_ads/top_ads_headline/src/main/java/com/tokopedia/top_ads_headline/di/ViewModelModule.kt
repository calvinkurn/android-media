package com.tokopedia.top_ads_headline.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.top_ads_headline.view.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AdDetailsViewModel::class)
    internal abstract fun provideAdDetailsViewModel(viewModel: AdDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsProductListViewModel::class)
    internal abstract fun provideTopAdsProductListViewModel(viewModel: TopAdsProductListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsHeadlineKeyViewModel::class)
    internal abstract fun provideTopAdsHeadlineKeyViewModel(viewModel: TopAdsHeadlineKeyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AdScheduleAndBudgetViewModel::class)
    internal abstract fun provideAdScheduleAndBudgetViewModel(viewModel: AdScheduleAndBudgetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SharedEditHeadlineViewModel::class)
    internal abstract fun provideSharedEditHeadlineViewModel(viewModel: SharedEditHeadlineViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditFormHeadlineViewModel::class)
    internal abstract fun provideEditFormHeadlineViewModel(viewModel: EditFormHeadlineViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HeadlineEditKeywordViewModel::class)
    internal abstract fun provideHeadlineEditKeywordViewModel(viewModel: HeadlineEditKeywordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditAdOthersViewModel::class)
    internal abstract fun provideEditAdOthersViewModel(viewModel: EditAdOthersViewModel): ViewModel

}