package com.tokopedia.home_recom.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.home_recom.viewmodel.RecommendationPageViewModel
import com.tokopedia.home_recom.viewmodel.SimilarProductRecommendationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * A class dagger module for handling viewModel
 */
@Module
abstract class ViewModelModule {
    @Binds
    @HomeRecommendationScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RecommendationPageViewModel::class)
    internal abstract fun recommendationItemViewModel(viewModel: RecommendationPageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SimilarProductRecommendationViewModel::class)
    internal abstract fun similarProductRecommendationViewModel(viewModel: SimilarProductRecommendationViewModel): ViewModel
}