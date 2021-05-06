package com.tokopedia.review.feature.inboxreview.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.inboxreview.di.scope.InboxReviewScope
import com.tokopedia.review.feature.inboxreview.presentation.viewmodel.InboxReviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class InboxReviewViewModelModule {

    @InboxReviewScope
    @Binds
    abstract fun bindViewModelInboxReviewFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(InboxReviewViewModel::class)
    abstract fun inboxReviewViewModel(inboxReviewViewModel: InboxReviewViewModel): ViewModel
}