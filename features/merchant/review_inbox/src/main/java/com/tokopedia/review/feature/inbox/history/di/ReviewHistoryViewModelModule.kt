package com.tokopedia.review.feature.inbox.history.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.inbox.history.presentation.viewmodel.ReviewHistoryViewModel
import com.tokopedia.review.feature.inbox.pending.di.ReviewPendingScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReviewHistoryViewModelModule {

    @Binds
    @ReviewPendingScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReviewHistoryViewModel::class)
    internal abstract fun reviewPendingViewModel(viewModel: ReviewHistoryViewModel): ViewModel
}