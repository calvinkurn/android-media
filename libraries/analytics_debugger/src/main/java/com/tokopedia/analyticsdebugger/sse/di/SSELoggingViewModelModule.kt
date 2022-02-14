package com.tokopedia.analyticsdebugger.sse.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.analyticsdebugger.sse.ui.viewmodel.SSELoggingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

/**
 * Created By : Jonathan Darwin on November 10, 2021
 */
@Module
abstract class SSELoggingViewModelModule {

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SSELoggingViewModel::class)
    abstract fun bindSSELoggingViewModel(viewModel: SSELoggingViewModel): ViewModel
}