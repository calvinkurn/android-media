package com.tokopedia.review.feature.credibility.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.credibility.presentation.viewmodel.ReviewCredibilityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewCredibilityViewModelModule {

    @Binds
    @ReviewCredibilityScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewCredibilityViewModel::class)
    internal abstract fun reviewCredibilityViewModel(viewModel: ReviewCredibilityViewModel): ViewModel
}