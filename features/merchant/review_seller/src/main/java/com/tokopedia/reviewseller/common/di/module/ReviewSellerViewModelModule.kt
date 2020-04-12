package com.tokopedia.reviewseller.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.reviewseller.common.di.scope.ReviewSellerScope
import com.tokopedia.reviewseller.feature.reviewlist.view.viewmodel.ReviewSellerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@ReviewSellerScope
@Module
abstract class ReviewSellerViewModelModule {

    @ReviewSellerScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewSellerViewModel::class)
    internal abstract fun reviewSellerViewModel(viewModel: ReviewSellerViewModel): ViewModel


}