package com.tokopedia.review.feature.historydetails.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.historydetails.presentation.viewmodel.ReviewDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ReviewDetailScope
abstract class ReviewDetailViewModelModule {

    @Binds
    @ReviewDetailScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewDetailViewModel::class)
    internal abstract fun reviewDetailViewModel(viewModel: ReviewDetailViewModel): ViewModel
}