package com.tokopedia.loginfingerprint.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.loginfingerprint.viewmodel.AccountChooserViewModel
import com.tokopedia.loginfingerprint.viewmodel.ScanFingerprintViewModel
import com.tokopedia.loginfingerprint.viewmodel.RegisterOnboardingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@LoginFingerprintSettingScope
abstract class LoginFingerprintViewModelsModule {
    @Binds
    @LoginFingerprintSettingScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ScanFingerprintViewModel::class)
    internal abstract fun fingerprintViewModel(viewModelScan: ScanFingerprintViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountChooserViewModel::class)
    internal abstract fun accountChooserViewModel(viewModel: AccountChooserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterOnboardingViewModel::class)
    internal abstract fun registerFingerprintViewModel(viewModel: RegisterOnboardingViewModel): ViewModel
}