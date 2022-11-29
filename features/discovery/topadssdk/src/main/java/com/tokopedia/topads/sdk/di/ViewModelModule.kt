package com.tokopedia.topads.sdk.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topads.sdk.viewmodel.TopAdsHeadlineViewModel
import com.tokopedia.topads.sdk.viewmodel.TopAdsImageViewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @TopAdsScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TopAdsImageViewViewModel::class)
    internal abstract fun provideTopAdsImageViewViewModel(viewModel: TopAdsImageViewViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TopAdsHeadlineViewModel::class)
    internal abstract fun provideTopAdsHeadlineViewModel(viewModel: TopAdsHeadlineViewModel): ViewModel
}
