package com.tokopedia.loginregister.registerinitial.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.loginregister.registerinitial.viewmodel.RegisterInitialViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@RegisterInitialScope
@Module
abstract class MockRegisterInitialViewModelModule{

    @Binds
    @RegisterInitialScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RegisterInitialViewModel::class)
    internal abstract fun registerInitialViewModel(viewModel: RegisterInitialViewModel): ViewModel
}