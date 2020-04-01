package com.tokopedia.home.account.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home.account.di.module.LogoutModule
import com.tokopedia.home.account.di.module.NotifQueryModule
import com.tokopedia.home.account.di.module.SettingsModule
import com.tokopedia.home.account.di.scope.AccountLogoutScope
import com.tokopedia.home.account.presentation.fragment.setting.GeneralSettingFragment

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
