package com.tokopedia.loginregister.login.behaviour.di

import com.tokopedia.loginregister.login.behaviour.activity.LoginActivityStub
import com.tokopedia.loginregister.login.behaviour.base.LoginBase
import com.tokopedia.loginregister.login.behaviour.di.modules.LoginUseCaseModuleStub
import com.tokopedia.loginregister.login.behaviour.di.modules.MockLoginRegisterComponent
import com.tokopedia.loginregister.login.di.*
import dagger.Component

@LoginScope
@Component(modules = [
    LoginModule::class,
    LoginQueryModule::class,
    LoginUseCaseModuleStub::class,
    LoginViewModelModule::class
], dependencies = [MockLoginRegisterComponent::class])
interface LoginComponentStub: LoginComponent {
    fun inject(activity: LoginActivityStub)
    fun inject(login: LoginBase)

//    fun inject(fragment: LoginEmailPhoneFragmentStub)
}