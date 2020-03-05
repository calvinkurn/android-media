package com.tokopedia.notifcenter.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.notifcenter.di.scope.NotificationTransactionScope
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationTransactionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module abstract class NotificationTransactionViewModelModule {

    @Binds
    @NotificationTransactionScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @NotificationTransactionScope
    @ViewModelKey(NotificationTransactionViewModel::class)
    internal abstract fun notificationTransactionViewModel(viewModel: NotificationTransactionViewModel): ViewModel

}