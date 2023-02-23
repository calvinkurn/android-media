package com.tokopedia.login_helper.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.login_helper.di.scope.LoginHelperScope
import com.tokopedia.login_helper.presentation.viewmodel.LoginHelperViewModel
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

}
