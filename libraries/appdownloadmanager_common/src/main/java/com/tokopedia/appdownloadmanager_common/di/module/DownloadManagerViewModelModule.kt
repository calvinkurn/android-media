package com.tokopedia.appdownloadmanager_common.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.appdownloadmanager_common.di.scope.DownloadManagerScope
import com.tokopedia.appdownloadmanager_common.presentation.viewmodel.DownloadManagerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class DownloadManagerViewModelModule {

    @DownloadManagerScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @DownloadManagerScope
    @Binds
    @IntoMap
    @ViewModelKey(DownloadManagerViewModel::class)
    abstract fun provideDownloadManagerViewModel(viewModel: DownloadManagerViewModel): ViewModel
}
