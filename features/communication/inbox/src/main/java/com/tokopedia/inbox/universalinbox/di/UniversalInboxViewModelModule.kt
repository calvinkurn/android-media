package com.tokopedia.inbox.universalinbox.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.inbox.universalinbox.view.UniversalInboxViewModel
import com.tokopedia.topads.sdk.viewmodel.TopAdsHeadlineViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UniversalInboxViewModelModule {
    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(
        viewModelFactory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(UniversalInboxViewModel::class)
    internal abstract fun bindUniversalInboxViewModel(
        viewModel: UniversalInboxViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(TopAdsHeadlineViewModel::class)
    internal abstract fun bindTopAdsHeadlineViewModel(
        viewModel: TopAdsHeadlineViewModel
    ): ViewModel
}
