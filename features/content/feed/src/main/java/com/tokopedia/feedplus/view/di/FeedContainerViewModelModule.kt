package com.tokopedia.feedplus.view.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.feedplus.view.presenter.FeedPlusContainerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@FeedContainerScope
abstract class FeedContainerViewModelModule {

    @FeedContainerScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FeedPlusContainerViewModel::class)
    internal abstract fun productInfoViewModel(viewModel: FeedPlusContainerViewModel): ViewModel
}