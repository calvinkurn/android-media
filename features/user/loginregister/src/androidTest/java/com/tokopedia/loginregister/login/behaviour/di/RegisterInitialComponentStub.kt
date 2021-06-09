package com.tokopedia.loginregister.login.behaviour.di

import com.tokopedia.loginregister.login.behaviour.base.RegisterInitialBase
import com.tokopedia.loginregister.login.behaviour.di.modules.MockLoginRegisterComponent
import com.tokopedia.loginregister.login.behaviour.di.modules.registerinitial.MockRegisterInitialuseCaseModule
import com.tokopedia.loginregister.registerinitial.di.*
import dagger.Component

@RegisterInitialScope
@Component(modules = [
    RegisterInitialModule::class,
    RegisterInitialQueryModule::class,
    MockRegisterInitialuseCaseModule::class,
    RegisterInitialViewModelModule::class
], dependencies = [MockLoginRegisterComponent::class])
interface RegisterInitialComponentStub: RegisterInitialComponent {
    fun inject(registerInitialBase: RegisterInitialBase)
}