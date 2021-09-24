package com.tokopedia.updateinactivephone.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.updateinactivephone.features.InactivePhoneViewModel
import com.tokopedia.updateinactivephone.features.accountlist.InactivePhoneAccountListViewModel
import com.tokopedia.updateinactivephone.features.submitnewphone.InactivePhoneDataUploadViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class InactivePhoneViewModelModule {

    @ActivityScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(InactivePhoneAccountListViewModel::class)
    abstract fun accountListViewModel(viewModelInactivePhone: InactivePhoneAccountListViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(InactivePhoneViewModel::class)
    abstract fun onboardingViewModel(viewModelInactivePhone: InactivePhoneViewModel): ViewModel

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(InactivePhoneDataUploadViewModel::class)
    abstract fun dataUploadViewModel(viewModelInactivePhone: InactivePhoneDataUploadViewModel): ViewModel
}