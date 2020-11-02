package com.tokopedia.top_ads_headline.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.top_ads_headline.view.viewmodel.AdContentViewModel
import com.tokopedia.top_ads_headline.view.viewmodel.AdDetailsViewModel
import com.tokopedia.top_ads_headline.view.viewmodel.TopAdsProductListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@HeadlineAdsScope
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
    @ViewModelKey(AdContentViewModel::class)
    internal abstract fun provideAdContentViewModel(viewModel: AdContentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsProductListViewModel::class)
    internal abstract fun provideTopAdsProductListViewModel(viewModel: TopAdsProductListViewModel): ViewModel
}