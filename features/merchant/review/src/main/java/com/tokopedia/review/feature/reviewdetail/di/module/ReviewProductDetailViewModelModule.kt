package com.tokopedia.review.feature.reviewdetail.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.reviewdetail.di.scope.ReviewDetailScope
import com.tokopedia.review.feature.reviewdetail.view.viewmodel.ProductReviewDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewProductDetailViewModelModule {

    @ReviewDetailScope
    @Binds
    abstract fun bindViewModelDetailFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProductReviewDetailViewModel::class)
    abstract fun reviewProductDetailViewModel(viewModelReviewDetail: ProductReviewDetailViewModel): ViewModel
}