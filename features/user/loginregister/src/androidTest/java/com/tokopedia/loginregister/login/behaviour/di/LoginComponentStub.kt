package com.tokopedia.loginregister.login.behaviour.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.loginregister.common.di.LoginRegisterModule
import com.tokopedia.loginregister.common.di.LoginRegisterScope
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.login.behaviour.di.modules.LoginUseCaseModuleStub
import com.tokopedia.loginregister.login.di.*
import com.tokopedia.sessioncommon.di.SessionCommonScope
import dagger.Component

@LoginRegisterScope
@SessionCommonScope
@LoginScope
@Component(modules = [
    LoginModule::class,
    LoginQueryModule::class,
    LoginUseCaseModuleStub::class,
    LoginViewModelModule::class,
    LoginRegisterModule::class,
], dependencies = [BaseAppComponent::class])
interface LoginComponentStub: LoginComponent {
    fun inject(login: LoginBase)

//    fun inject(fragment: LoginEmailPhoneFragmentStub)
}
