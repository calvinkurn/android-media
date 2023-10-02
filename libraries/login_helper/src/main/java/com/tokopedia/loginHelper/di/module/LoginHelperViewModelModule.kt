package com.tokopedia.loginHelper.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.loginHelper.di.scope.LoginHelperScope
import com.tokopedia.loginHelper.presentation.accountSettings.viewmodel.LoginHelperAccountSettingsViewModel
import com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.LoginHelperAddEditAccountViewModel
import com.tokopedia.loginHelper.presentation.home.viewmodel.LoginHelperViewModel
import com.tokopedia.loginHelper.presentation.searchAccount.viewmodel.LoginHelperSearchAccountViewModel
import dagger.Binds
import dagger.multibindings.IntoMap

@dagger.Module
abstract class LoginHelperViewModelModule {

    @LoginHelperScope
    @Binds
    internal abstract fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginHelperViewModel::class)
    internal abstract fun provideLoginHelperViewModel(viewModel: LoginHelperViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginHelperAccountSettingsViewModel::class)
    internal abstract fun provideLoginHelperAccountSettingsViewModel(viewModel: LoginHelperAccountSettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginHelperAddEditAccountViewModel::class)
    internal abstract fun provideLoginHelperAddEditAccountViewModel(viewModel: LoginHelperAddEditAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginHelperSearchAccountViewModel::class)
    internal abstract fun provideLoginHelperSearchAccountViewModel(viewModel: LoginHelperSearchAccountViewModel): ViewModel
}
