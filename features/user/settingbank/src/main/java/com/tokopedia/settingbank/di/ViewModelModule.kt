package com.tokopedia.settingbank.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.settingbank.view.viewModel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
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
    @ViewModelKey(SelectBankViewModel::class)
    internal abstract fun addSelectBankViewModel(viewModel: SelectBankViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddAccountViewModel::class)
    internal abstract fun addAddAccountViewModel(viewModel: AddAccountViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(UploadDocumentViewModel::class)
    internal abstract fun addUploadDocumentViewModel(viewModel: UploadDocumentViewModel): ViewModel

}