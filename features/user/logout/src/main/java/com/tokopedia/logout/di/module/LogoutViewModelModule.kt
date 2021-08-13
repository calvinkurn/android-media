package com.tokopedia.logout.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.logout.di.LogoutScope
import com.tokopedia.logout.viewmodel.LogoutViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LogoutViewModelModule {

    @LogoutScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @LogoutScope
    @Binds
    @IntoMap
    @ViewModelKey(LogoutViewModel::class)
    internal abstract fun logutViewModel(viewModel: LogoutViewModel): ViewModel
}