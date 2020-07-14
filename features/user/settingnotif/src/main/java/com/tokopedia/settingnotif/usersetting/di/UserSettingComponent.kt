package com.tokopedia.settingnotif.usersetting.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.settingnotif.usersetting.di.module.UserSettingModule
import com.tokopedia.settingnotif.usersetting.di.module.UserSettingVIewModelModule
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment
import dagger.Component

@UserSettingScope
@Component(modules = [
    UserSettingModule::class,
    UserSettingVIewModelModule::class
], dependencies = [
    BaseAppComponent::class
])
interface UserSettingComponent {
    fun inject(fragment: SettingFieldFragment)
}