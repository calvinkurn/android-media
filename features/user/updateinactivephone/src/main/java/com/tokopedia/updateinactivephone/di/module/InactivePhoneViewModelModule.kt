package com.tokopedia.updateinactivephone.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.updateinactivephone.di.InactivePhoneScope
import com.tokopedia.updateinactivephone.view.viewmodel.InactivePhoneAccountListViewModel
import com.tokopedia.updateinactivephone.view.viewmodel.InactivePhoneDataUploadViewModel
import com.tokopedia.updateinactivephone.view.viewmodel.InactivePhoneOnboardingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class InactivePhoneViewModelModule {

    @InactivePhoneScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @InactivePhoneScope
    @Binds
    @IntoMap
    @ViewModelKey(InactivePhoneAccountListViewModel::class)
    abstract fun accountListViewModel(viewModelInactivePhone: InactivePhoneAccountListViewModel): ViewModel

    @InactivePhoneScope
    @Binds
    @IntoMap
    @ViewModelKey(InactivePhoneOnboardingViewModel::class)
    abstract fun onboardingViewModel(viewModelInactivePhone: InactivePhoneOnboardingViewModel): ViewModel

    @InactivePhoneScope
    @Binds
    @IntoMap
    @ViewModelKey(InactivePhoneDataUploadViewModel::class)
    abstract fun dataUploadViewModel(viewModelInactivePhone: InactivePhoneDataUploadViewModel): ViewModel
}