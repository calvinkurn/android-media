package com.tokopedia.notifcenter.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.notifcenter.view.NotificationViewModel
import com.tokopedia.topads.sdk.viewmodel.TopAdsHeadlineViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NotificationViewModelModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(NotificationViewModel::class)
    internal abstract fun notificationViewModel(viewModel: NotificationViewModel): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(TopAdsHeadlineViewModel::class)
    internal abstract fun provideTopAdsHeadlineViewModel(viewModel: TopAdsHeadlineViewModel): ViewModel

}
