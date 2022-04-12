package com.tokopedia.inbox.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.inbox.viewmodel.InboxViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class InboxViewModelModule {

    @Binds
    @InboxScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @InboxScope
    @ViewModelKey(InboxViewModel::class)
    internal abstract fun notificationInboxViewModel(viewModel: InboxViewModel): ViewModel

}