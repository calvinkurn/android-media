package com.tokopedia.managepassword.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.managepassword.addpassword.view.viewmodel.AddPasswordViewModel
import com.tokopedia.managepassword.changepassword.view.viewmodel.ChangePasswordViewModel
import com.tokopedia.managepassword.di.ManagePasswordScope
import com.tokopedia.managepassword.forgotpassword.view.viewmodel.ForgotPasswordViewModel
import com.tokopedia.managepassword.haspassword.view.viewmode.HasPasswordViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ManagePasswordViewModelModule {

    @Binds
    @ManagePasswordScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ManagePasswordScope
    @Binds
    @IntoMap
    @ViewModelKey(HasPasswordViewModel::class)
    internal abstract fun hasPasswordViewModel(viewModel: HasPasswordViewModel): ViewModel

    @ManagePasswordScope
    @Binds
    @IntoMap
    @ViewModelKey(AddPasswordViewModel::class)
    internal abstract fun addPasswordViewModel(viewModel: AddPasswordViewModel): ViewModel

    @ManagePasswordScope
    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    internal abstract fun forgotPasswordViewModel(viewModel: ForgotPasswordViewModel): ViewModel

    @ManagePasswordScope
    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    internal abstract fun changePasswordViewModel(viewModel: ChangePasswordViewModel): ViewModel
}