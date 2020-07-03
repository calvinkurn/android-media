package com.tokopedia.topads.sdk.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.topads.sdk.viewmodel.TopAdsImageViewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@TopAdsScope
abstract class TopAdsViewModelModule {

    @TopAdsScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @TopAdsScope
    @ViewModelKey(TopAdsImageViewViewModel::class)
    internal abstract fun topAdsImageViewViewModel(viewModel: TopAdsImageViewViewModel): ViewModel
}