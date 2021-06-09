package com.tokopedia.loginregister.login.behaviour.di

import com.tokopedia.loginregister.login.behaviour.base.RegisterInitialBase
import com.tokopedia.loginregister.login.behaviour.di.modules.MockLoginRegisterComponent
import com.tokopedia.loginregister.login.di.LoginScope
import com.tokopedia.loginregister.registerinitial.di.*
import dagger.Component

@LoginScope
@RegisterInitialScope
@Component(modules = [
    RegisterInitialModule::class,
    RegisterInitialQueryModule::class,
    RegisterInitialUseCaseModule::class,
    RegisterInitialViewModelModule::class
], dependencies = [MockLoginRegisterComponent::class])
interface RegisterInitialComponentStub: RegisterInitialComponent {
    fun inject(registerInitialBase: RegisterInitialBase)
}