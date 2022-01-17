package com.tokopedia.review.feature.inbox.buyerreview.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.review.common.reviewreplyinsert.di.module.ReviewReplyInsertViewModelModule
import dagger.Binds
import dagger.Module

@Module(includes = [ReviewReplyInsertViewModelModule::class])
abstract class ReputationViewModelModule {
    @Binds
    @ReputationScope
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}