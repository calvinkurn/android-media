package com.tokopedia.reviewseller.feature.reviewlist.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.reviewseller.feature.reviewlist.di.scope.ReviewProductListScope
import com.tokopedia.reviewseller.feature.reviewlist.view.viewmodel.ReviewSellerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ReviewProductListScope
abstract class ReviewSellerViewModelModule {

    @ReviewProductListScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewSellerViewModel::class)
    internal abstract fun reviewSellerViewModel(viewModel: ReviewSellerViewModel): ViewModel
}