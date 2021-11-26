package com.tokopedia.managepassword.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.managepassword.addpassword.view.viewmodel.AddPasswordViewModel
import com.tokopedia.managepassword.haspassword.view.viewmodel.HasPasswordViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ManagePasswordViewModelModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(HasPasswordViewModel::class)
    internal abstract fun hasPasswordViewModel(viewModel: HasPasswordViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(AddPasswordViewModel::class)
    internal abstract fun addPasswordViewModel(viewModel: AddPasswordViewModel): ViewModel
}