package com.tokopedia.loginregister.inactive_phone_number.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.inactive_phone_number.view.viewmodel.InactivePhoneNumberViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class InactivePhoneNumberViewModelModule {
    @Binds
    @ActivityScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(InactivePhoneNumberViewModel::class)
    internal abstract fun provideViewModel(viewModel: InactivePhoneNumberViewModel): ViewModel
}