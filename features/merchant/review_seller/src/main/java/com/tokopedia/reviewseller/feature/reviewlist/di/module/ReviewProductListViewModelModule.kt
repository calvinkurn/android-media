package com.tokopedia.reviewseller.feature.reviewlist.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.reviewseller.common.di.scope.ReviewSellerScope
import com.tokopedia.reviewseller.feature.reviewlist.view.viewmodel.SellerReviewListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@ReviewSellerScope
abstract class ReviewProductListViewModelModule {

    @ReviewSellerScope
    @Binds
    abstract fun bindViewModelProductListFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SellerReviewListViewModel::class)
    abstract fun reviewProductListViewModel(viewModelListReviewList: SellerReviewListViewModel): ViewModel
}