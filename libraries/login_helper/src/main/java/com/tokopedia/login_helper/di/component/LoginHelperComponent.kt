package com.tokopedia.login_helper.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.login_helper.di.scope.LoginHelperScope
import com.tokopedia.login_helper.presentation.fragment.LoginHelperFragment
import dagger.Component

@LoginHelperScope
@Component(
    modules = [LoginHelperModule::class, LoginHelperViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface LoginHelperComponent {
    fun inject(loginHelperFragment: LoginHelperFragment)
}
