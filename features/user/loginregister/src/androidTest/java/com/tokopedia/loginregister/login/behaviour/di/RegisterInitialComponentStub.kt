package com.tokopedia.loginregister.login.behaviour.di

import com.tokopedia.loginregister.login.behaviour.base.RegisterEmailBase
import com.tokopedia.loginregister.login.behaviour.base.RegisterInitialBase
import com.tokopedia.loginregister.login.behaviour.di.modules.MockLoginRegisterComponent
import com.tokopedia.loginregister.login.behaviour.di.modules.registerinitial.MockRegisterInitialModule
import com.tokopedia.loginregister.login.behaviour.di.modules.registerinitial.MockRegisterInitialuseCaseModule
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialComponent
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialQueryModule
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialScope
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialViewModelModule
import dagger.Component

@RegisterInitialScope
@Component(modules = [
    MockRegisterInitialModule::class,
    RegisterInitialQueryModule::class,
    MockRegisterInitialuseCaseModule::class,
    RegisterInitialViewModelModule::class
], dependencies = [MockLoginRegisterComponent::class])
interface RegisterInitialComponentStub: RegisterInitialComponent {
    fun inject(registerInitialBase: RegisterInitialBase)
    fun inject(registerEmailBase: RegisterEmailBase)
}