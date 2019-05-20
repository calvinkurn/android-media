package com.tokopedia.home_recom.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.recommendation_widget_common.viewmodel.RecommendationItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@HomeRecommendationScope
abstract class ViewModelModule {
    @Binds
    @HomeRecommendationScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RecommendationItemViewModel::class)
    internal abstract fun recommendationItemViewModel(viewModel: RecommendationItemViewModel): ViewModel
}