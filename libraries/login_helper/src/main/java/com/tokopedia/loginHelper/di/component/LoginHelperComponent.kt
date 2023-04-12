package com.tokopedia.loginHelper.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.loginHelper.di.module.LoginHelperModule
import com.tokopedia.loginHelper.di.module.LoginHelperViewModelModule
import com.tokopedia.loginHelper.di.scope.LoginHelperScope
import com.tokopedia.loginHelper.presentation.accountSettings.fragment.LoginHelperAccountSettingsFragment
import com.tokopedia.loginHelper.presentation.addEditAccount.fragment.LoginHelperAddEditAccountFragment
import com.tokopedia.loginHelper.presentation.searchAccount.fragment.LoginHelperSearchAccountFragment
import com.tokopedia.loginHelper.presentation.home.fragment.LoginHelperFragment
import dagger.Component

@LoginHelperScope
@Component(
    modules = [LoginHelperModule::class, LoginHelperViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface LoginHelperComponent {
    fun inject(loginHelperFragment: LoginHelperFragment)

    fun inject(loginHelperAccountSettingsFragment: LoginHelperAccountSettingsFragment)

    fun inject(loginHelperAddEditAccountFragment: LoginHelperAddEditAccountFragment)

    fun inject(loginHelperSearchAccountFragment: LoginHelperSearchAccountFragment)
}
