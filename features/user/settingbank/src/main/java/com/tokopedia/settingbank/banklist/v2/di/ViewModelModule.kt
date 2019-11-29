package com.tokopedia.settingbank.banklist.v2.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.settingbank.banklist.v2.view.viewModel.BankNumberTextWatcherViewModel
import com.tokopedia.settingbank.banklist.v2.view.viewModel.SelectBankViewModel
import com.tokopedia.settingbank.banklist.v2.view.viewModel.SettingBankTNCViewModel
import com.tokopedia.settingbank.banklist.v2.view.viewModel.SettingBankViewModel
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

}