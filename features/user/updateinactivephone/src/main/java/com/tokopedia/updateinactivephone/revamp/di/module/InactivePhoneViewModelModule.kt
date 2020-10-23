package com.tokopedia.updateinactivephone.revamp.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.updateinactivephone.revamp.di.InactivePhoneScope
import com.tokopedia.updateinactivephone.revamp.view.viewmodel.AccountListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class InactivePhoneViewModelModule {

    @InactivePhoneScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @InactivePhoneScope
    @Binds
    @IntoMap
    @ViewModelKey(AccountListViewModel::class)
    internal abstract fun accountListViewModel(viewModel: AccountListViewModel): ViewModel
}