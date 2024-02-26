package com.tokopedia.loginregister.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.registerinitial.base.RegisterEmailBase
import com.tokopedia.loginregister.registerinitial.base.RegisterInitialBase
import com.tokopedia.loginregister.di.modules.FakeRegisterInitialuseCaseModule
import com.tokopedia.loginregister.registerinitial.di.*
import dagger.Component

@ActivityScope
@Component(modules = [
    RegisterInitialModule::class,
    RegisterInitialQueryModule::class,
    FakeRegisterInitialuseCaseModule::class,
    RegisterInitialViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface RegisterInitialComponentStub: RegisterInitialComponent {
    fun inject(registerInitialBase: RegisterInitialBase)
    fun inject(registerEmailBase: RegisterEmailBase)
}
