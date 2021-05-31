package com.tokopedia.review.feature.reviewreply.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.reviewreply.di.scope.ReviewReplyScope
import com.tokopedia.review.feature.reviewreply.view.viewmodel.SellerReviewReplyViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewReplyViewModelModule {

    @ReviewReplyScope
    @Binds
    abstract fun bindViewModelDetailFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SellerReviewReplyViewModel::class)
    abstract fun reviewReplyViewModelModule(viewModelReviewReply: SellerReviewReplyViewModel): ViewModel
}