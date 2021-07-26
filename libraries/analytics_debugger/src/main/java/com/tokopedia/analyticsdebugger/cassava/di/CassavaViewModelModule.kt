package com.tokopedia.analyticsdebugger.cassava.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.analyticsdebugger.cassava.debugger.DebuggerListViewModel
import com.tokopedia.analyticsdebugger.cassava.validator.main.ValidatorViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

/**
 * @author by furqan on 07/04/2021
 */
@Module
abstract class CassavaViewModelModule {

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ValidatorViewModel::class)
    abstract fun validatorViewModel(viewModel: ValidatorViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DebuggerListViewModel::class)
    abstract fun debuggerViewModel(viewModel: DebuggerListViewModel): ViewModel
}