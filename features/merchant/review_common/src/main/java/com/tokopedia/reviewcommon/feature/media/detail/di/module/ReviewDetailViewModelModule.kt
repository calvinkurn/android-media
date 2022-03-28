package com.tokopedia.reviewcommon.feature.media.detail.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.reviewcommon.feature.media.detail.di.qualifier.ReviewDetailViewModelFactory
import com.tokopedia.reviewcommon.feature.media.detail.di.scope.ReviewDetailScope
import com.tokopedia.reviewcommon.feature.media.detail.presentation.viewmodel.ReviewDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewDetailViewModelModule {
    @Binds
    @ReviewDetailScope
    @ReviewDetailViewModelFactory
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewDetailViewModel::class)
    internal abstract fun provideReviewDetailViewModel(viewModel: ReviewDetailViewModel): ViewModel
}