package com.tokopedia.review.feature.inbox.buyerreview.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel.InboxReputationDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReputationViewModelModule {
    @Binds
    @ReputationScope
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(InboxReputationDetailViewModel::class)
    internal abstract fun provideInboxReputationDetailViewModel(viewModel: InboxReputationDetailViewModel): ViewModel
}