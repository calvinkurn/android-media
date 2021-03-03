package com.tokopedia.tkpd.tkpdreputation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.tkpd.tkpdreputation.review.product.view.viewmodel.ReviewProductViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewProductViewModelModule {
    @ReputationScope
    @Binds
    abstract fun bindViewModelDetailFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewProductViewModel::class)
    abstract fun reviewProductViewModel(viewModelReviewProduct: ReviewProductViewModel): ViewModel
}