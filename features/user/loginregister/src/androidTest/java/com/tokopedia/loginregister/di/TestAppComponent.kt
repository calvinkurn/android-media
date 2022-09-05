package com.tokopedia.loginregister.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.loginregister.di.modules.AppModuleStub
import dagger.Component

@ApplicationScope
@Component(modules = [AppModuleStub::class])
interface TestAppComponent : BaseAppComponent