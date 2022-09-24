package com.tokopedia.loginregister.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.di.modules.LoginUseCaseModuleStub
import com.tokopedia.loginregister.login.di.LoginComponent
import com.tokopedia.loginregister.login.di.LoginModule
import com.tokopedia.loginregister.login.di.LoginViewModelModule
import dagger.Component

@ActivityScope
@Component(modules = [
    LoginModule::class,
    LoginUseCaseModuleStub::class,
    LoginViewModelModule::class,
], dependencies = [BaseAppComponent::class])
interface LoginComponentStub: LoginComponent {
    fun inject(login: LoginBase)
}
