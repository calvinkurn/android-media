package com.tokopedia.settingbank.banklist.v2.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.settingbank.banklist.v2.view.viewModel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@SettingBankScope
abstract class ViewModelModule {

    @SettingBankScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SettingBankViewModel::class)
    internal abstract fun settingBankViewModel(viewModel: SettingBankViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingBankTNCViewModel::class)
    internal abstract fun addBankTermsConditionViewModel(viewModel: SettingBankTNCViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectBankViewModel::class)
    internal abstract fun addSelectBankViewModel(viewModel: SelectBankViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BankNumberTextWatcherViewModel::class)
    internal abstract fun addAccountNumberTextWatcherViewModel(viewModel: BankNumberTextWatcherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheckAccountNumberViewModel::class)
    internal abstract fun checkAccountNumberViewModel(viewModel: CheckAccountNumberViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountHolderNameViewModel::class)
    internal abstract fun validateAccountViewModel(viewModel: AccountHolderNameViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddAccountViewModel::class)
    internal abstract fun addAddAccountViewModel(viewModel: AddAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MakeAccountPrimaryViewModel::class)
    internal abstract fun addMakeAccountPrimaryViewModel(viewModel: MakeAccountPrimaryViewModel): ViewModel

}