package com.tokopedia.home.beranda.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.presentation.viewModel.HomeRecommendationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(HomeRecommendationViewModel::class)
    internal abstract fun homeRecommendationViewModel(viewModel: HomeRecommendationViewModel): ViewModel
}