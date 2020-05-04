package com.tokopedia.revamp_changepassword.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.revamp_changepassword.di.ChangePasswordScope
import com.tokopedia.revamp_changepassword.view.viewmode.HasPasswordViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChangePasswordViewModelModule {

    @Binds
    @ChangePasswordScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ChangePasswordScope
    @Binds
    @IntoMap
    @ViewModelKey(HasPasswordViewModel::class)
    internal abstract fun changePasswordViewModel(viewModel: HasPasswordViewModel): ViewModel
}