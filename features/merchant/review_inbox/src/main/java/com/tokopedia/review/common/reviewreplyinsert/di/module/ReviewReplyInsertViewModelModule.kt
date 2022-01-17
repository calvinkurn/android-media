package com.tokopedia.review.common.reviewreplyinsert.di.module

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.common.reviewreplyinsert.presentation.viewmodel.ReviewReplyInsertViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewReplyInsertViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ReviewReplyInsertViewModel::class)
    internal abstract fun provideReviewReplyInsertViewModel(viewModel: ReviewReplyInsertViewModel): ViewModel
}