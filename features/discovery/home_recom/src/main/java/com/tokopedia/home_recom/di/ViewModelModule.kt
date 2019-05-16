package com.tokopedia.home_recom.di

import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
@HomeRecommendationScope
abstract class ViewModelModule {
    @Binds
    @HomeRecommendationScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}