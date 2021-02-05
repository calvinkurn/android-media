package com.tokopedia.review.feature.inbox.common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.review.feature.inbox.common.di.scope.InboxReputationScope
import com.tokopedia.review.feature.inbox.common.presentation.viewmodel.InboxReputationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class InboxReputationViewModelModule {

    @InboxReputationScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(InboxReputationViewModel::class)
    abstract fun inboxReputationViewModel(inboxReputationViewModel: InboxReputationViewModel): ViewModel

}
