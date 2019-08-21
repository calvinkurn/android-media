package com.tokopedia.home.account.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home.account.di.module.EmailNotificationModule
import com.tokopedia.home.account.di.scope.EmailNotifScope
import com.tokopedia.home.account.presentation.fragment.setting.EmailNotificationSettingFragment

import dagger.Component

@EmailNotifScope
@Component(modules = [EmailNotificationModule::class], dependencies = [BaseAppComponent::class])
interface EmailNotificationSettingComponent {

    fun inject(fragment: EmailNotificationSettingFragment)
}
