package com.tokopedia.analyticsdebugger.serverlogger.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.analyticsdebugger.serverlogger.di.scope.ServerLoggerScope
import com.tokopedia.analyticsdebugger.serverlogger.presentation.viewmodel.ServerLoggerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ServerLoggerViewModelModule {

    @ServerLoggerScope
    @Binds
    abstract fun bindServerLoggerViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ServerLoggerViewModel::class)
    abstract fun bindServerLoggerViewModel(serverLoggerViewModel: ServerLoggerViewModel): ViewModel
}