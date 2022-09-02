package com.tokopedia.loginregister.redefine_register_email.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.redefine_register_email.input_phone.view.viewmodel.RedefineRegisterInputPhoneViewModel
import com.tokopedia.loginregister.redefine_register_email.register_email.view.viewmodel.RedefineRegisterEmailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class RedefineRegisterEmailViewModelModule {

    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RedefineRegisterEmailViewModel::class)
    internal abstract fun bindRedefineRegisterEmailViewModel(viewModel: RedefineRegisterEmailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedefineRegisterInputPhoneViewModel::class)
    internal abstract fun bindRedefineInputPhoneViewModel(viewModel: RedefineRegisterInputPhoneViewModel): ViewModel

}