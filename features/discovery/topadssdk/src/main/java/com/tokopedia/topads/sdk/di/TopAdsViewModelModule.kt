package com.tokopedia.topads.sdk.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.basemvvm.viewmodel.ViewModelKey
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import com.tokopedia.topads.sdk.viewmodel.TopAdsImageViewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@TopAdsScope
abstract class TopAdsViewModelModule {

    @TopAdsScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @TopAdsScope
    @ViewModelKey(TopAdsImageViewViewModel::class)
    internal abstract fun topAdsImageViewViewModel(viewModel: TopAdsImageViewViewModel): ViewModel
}