package com.tokopedia.managepassword.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.managepassword.di.ManagePasswordScope
import com.tokopedia.managepassword.view.viewmode.HasPasswordViewModel
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
    internal abstract fun changePasswordViewModel(viewModel: HasPasswordViewModel): ViewModel
}