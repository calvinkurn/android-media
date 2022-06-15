package com.tokopedia.loginregister.login.behaviour.di

import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.login.behaviour.base.RegisterEmailBase
import com.tokopedia.loginregister.login.behaviour.base.RegisterInitialBase
import com.tokopedia.loginregister.login.behaviour.di.modules.MockLoginRegisterComponent
import com.tokopedia.loginregister.login.behaviour.di.modules.registerinitial.MockRegisterInitialModule
import com.tokopedia.loginregister.login.behaviour.di.modules.registerinitial.MockRegisterInitialuseCaseModule
import com.tokopedia.loginregister.registerinitial.di.*
import dagger.Component

@RegisterInitialScope
@Component(modules = [
    RegisterInitialModule::class,
    RegisterInitialQueryModule::class,
    RegisterInitialUseCaseModule::class,
    RegisterInitialViewModelModule::class
], dependencies = [LoginRegisterComponent::class])
interface RegisterInitialComponentStub: RegisterInitialComponent {
    fun inject(registerInitialBase: RegisterInitialBase)
    fun inject(registerEmailBase: RegisterEmailBase)
}