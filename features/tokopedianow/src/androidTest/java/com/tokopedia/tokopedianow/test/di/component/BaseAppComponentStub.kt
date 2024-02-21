package com.tokopedia.tokopedianow.test.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.tokopedianow.test.di.module.AppModuleStub
import dagger.Component

@ApplicationScope
@Component(modules = [AppModuleStub::class])
interface BaseAppComponentStub: BaseAppComponent
