package com.tokopedia.notifcenter.presentation.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.notifcenter.presentation.di.scope.NotificationTransactionScope
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationTransactionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@NotificationTransactionScope
abstract class NotificationTransactionViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(NotificationTransactionViewModel::class)
    internal abstract fun notificationTransactionViewModel(viewModel: NotificationTransactionViewModel): ViewModel

}