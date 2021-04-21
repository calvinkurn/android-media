package com.tokopedia.loginregister.login.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.loginregister.login.view.viewmodel.LoginEmailPhoneViewModel
import com.tokopedia.loginregister.login.view.viewmodel.SellerSeamlessViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LoginViewModelModule {
    @Binds
    @LoginScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginEmailPhoneViewModel::class)
    internal abstract fun provideLoginEmailPhoneViewModel(viewModel: LoginEmailPhoneViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SellerSeamlessViewModel::class)
    internal abstract fun provideSellerSeamlessViewModel(viewModel: SellerSeamlessViewModel): ViewModel
}