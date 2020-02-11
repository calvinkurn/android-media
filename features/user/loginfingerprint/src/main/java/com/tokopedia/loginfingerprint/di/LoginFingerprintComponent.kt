package com.tokopedia.loginfingerprint.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.loginfingerprint.view.AccountChooserDialog
import com.tokopedia.loginfingerprint.view.ScanFingerprintDialog
import com.tokopedia.loginfingerprint.view.fragment.RegisterFingerprintOnboardingFragment
import dagger.Component


@LoginFingerprintSettingScope
@Component(modules = [
    LoginFingerprintSettingModule::class,
    LoginFingerprintQueryModule::class,
    LoginFingerprintViewModelsModule::class
], dependencies = [BaseAppComponent::class])
interface LoginFingerprintComponent {
    fun inject(fragment: ScanFingerprintDialog)
    fun inject(fragment: AccountChooserDialog)
    fun inject(fragment: RegisterFingerprintOnboardingFragment)
}