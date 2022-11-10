package com.tokopedia.notifcenter.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.presentation.viewmodel.NotificationViewModel
import com.tokopedia.topads.sdk.viewmodel.TopAdsHeadlineViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module abstract class NotificationViewModelModule {

    @Binds
    @NotificationScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @NotificationScope
    @ViewModelKey(NotificationViewModel::class)
    internal abstract fun notificationViewModel(viewModel: NotificationViewModel): ViewModel

    @Binds
    @IntoMap
    @NotificationScope
    @ViewModelKey(TopAdsHeadlineViewModel::class)
    internal abstract fun provideTopAdsHeadlineViewModel(viewModel: TopAdsHeadlineViewModel): ViewModel

}
