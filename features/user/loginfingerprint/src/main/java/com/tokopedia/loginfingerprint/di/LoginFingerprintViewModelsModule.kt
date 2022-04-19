package com.tokopedia.loginfingerprint.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.loginfingerprint.viewmodel.FingerprintLandingViewModel
import com.tokopedia.loginfingerprint.viewmodel.SettingFingerprintViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LoginFingerprintViewModelsModule {
    @Binds
    @LoginFingerprintSettingScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SettingFingerprintViewModel::class)
    internal abstract fun settingFingerprintViewModel(viewModel: SettingFingerprintViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FingerprintLandingViewModel::class)
    internal abstract fun landingFingerprintViewModel(viewModel: FingerprintLandingViewModel): ViewModel
}