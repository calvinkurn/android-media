package com.tokopedia.loginregister.login.behaviour.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.login.behaviour.base.RegisterEmailBase
import com.tokopedia.loginregister.login.behaviour.base.RegisterInitialBase
import com.tokopedia.loginregister.login.behaviour.di.modules.MockLoginRegisterComponent
import com.tokopedia.loginregister.login.behaviour.di.modules.registerinitial.MockRegisterInitialModule
import com.tokopedia.loginregister.login.behaviour.di.modules.registerinitial.MockRegisterInitialuseCaseModule
import com.tokopedia.loginregister.registerinitial.di.*
import dagger.Component

@ActivityScope
@Component(modules = [
    RegisterInitialModule::class,
    RegisterInitialQueryModule::class,
    MockRegisterInitialuseCaseModule::class,
    RegisterInitialViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface RegisterInitialComponentStub: RegisterInitialComponent {
    fun inject(registerInitialBase: RegisterInitialBase)
    fun inject(registerEmailBase: RegisterEmailBase)
}