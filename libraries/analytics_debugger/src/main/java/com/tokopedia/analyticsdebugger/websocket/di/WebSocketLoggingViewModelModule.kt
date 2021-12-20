package com.tokopedia.analyticsdebugger.websocket.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analyticsdebugger.websocket.ui.viewmodel.WebSocketLoggingViewModel
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */

@Module
abstract class WebSocketLoggingViewModelModule {
    @Singleton
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @WebSocketLoggingScope
    @Binds
    abstract fun bindWebSocketLoggingViewModel(viewModel: WebSocketLoggingViewModel): ViewModel
}