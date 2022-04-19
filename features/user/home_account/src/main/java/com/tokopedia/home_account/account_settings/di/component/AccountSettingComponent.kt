package com.tokopedia.home_account.account_settings.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home_account.account_settings.di.module.AccountSettingModule
import com.tokopedia.home_account.account_settings.di.scope.AccountSettingScope
import com.tokopedia.home_account.account_settings.presentation.fragment.setting.AccountSettingFragment

import dagger.Component

/**
 * @author by alvinatin on 16/11/18.
 */

@Component(modules = [AccountSettingModule::class], dependencies = [BaseAppComponent::class])
@AccountSettingScope
interface AccountSettingComponent {
    fun inject(fragment: AccountSettingFragment)
}
