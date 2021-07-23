package com.tokopedia.loginfingerprint.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.loginfingerprint.view.activity.VerifyFingerprintActivity
import com.tokopedia.loginfingerprint.view.fragment.SettingFingerprintFragment
import com.tokopedia.sessioncommon.di.SessionCommonScope
import com.tokopedia.sessioncommon.di.SessionModule
import dagger.Component


@LoginFingerprintSettingScope
@SessionCommonScope
@Component(modules = [
    LoginFingerprintSettingModule::class,
    LoginFingerprintQueryModule::class,
    LoginFingerprintViewModelsModule::class,
    SessionModule::class
], dependencies = [BaseAppComponent::class])
interface LoginFingerprintComponent {
    fun inject(fingerprintFragment: SettingFingerprintFragment)
    fun inject(verifyFingerprintActivity: VerifyFingerprintActivity)
}