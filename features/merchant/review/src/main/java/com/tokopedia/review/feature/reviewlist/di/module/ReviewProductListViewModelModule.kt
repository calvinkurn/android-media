package com.tokopedia.review.feature.reviewlist.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.reviewlist.di.scope.ReviewProductListScope
import com.tokopedia.review.feature.reviewlist.view.viewmodel.SellerReviewListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewProductListViewModelModule {

    @ReviewProductListScope
    @Binds
    abstract fun bindViewModelProductListFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SellerReviewListViewModel::class)
    abstract fun reviewProductListViewModel(viewModelListReviewList: SellerReviewListViewModel): ViewModel
}