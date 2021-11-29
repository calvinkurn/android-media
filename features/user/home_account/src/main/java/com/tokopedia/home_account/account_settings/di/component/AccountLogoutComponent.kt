package com.tokopedia.home_account.account_settings.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home_account.account_settings.di.module.LogoutModule
import com.tokopedia.home_account.account_settings.di.module.NotifQueryModule
import com.tokopedia.home_account.account_settings.di.module.SettingsModule
import com.tokopedia.home_account.account_settings.di.scope.AccountLogoutScope
import com.tokopedia.home_account.account_settings.presentation.fragment.setting.GeneralSettingFragment

import dagger.Component

@AccountLogoutScope
@Component(modules = [
    SettingsModule::class,
    LogoutModule::class,
    NotifQueryModule::class
], dependencies = [BaseAppComponent::class])
interface AccountLogoutComponent {
    fun inject(fragment: GeneralSettingFragment)
}
