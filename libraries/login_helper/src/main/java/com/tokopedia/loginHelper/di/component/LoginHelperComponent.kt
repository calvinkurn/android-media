package com.tokopedia.loginHelper.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.loginHelper.di.module.LoginHelperModule
import com.tokopedia.loginHelper.di.module.LoginHelperViewModelModule
import com.tokopedia.loginHelper.di.scope.LoginHelperScope
import com.tokopedia.loginHelper.presentation.home.fragment.LoginHelperFragment
import dagger.Component

@LoginHelperScope
@Component(
    modules = [LoginHelperModule::class, LoginHelperViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface LoginHelperComponent {
    fun inject(loginHelperFragment: LoginHelperFragment)
}
