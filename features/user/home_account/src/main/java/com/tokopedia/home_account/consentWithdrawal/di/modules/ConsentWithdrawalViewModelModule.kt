package com.tokopedia.home_account.consentWithdrawal.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.consentWithdrawal.viewmodel.ConsentWithdrawalViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ConsentWithdrawalViewModelModule {

    @Binds
    @ActivityScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ConsentWithdrawalViewModel::class)
    abstract fun provideConsentWithdrawalViewModel(viewModel: ConsentWithdrawalViewModel): ViewModel
}
