package com.tokopedia.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import dagger.Component


@ApplicationScope
@Component(modules = [AppModuleStub::class])
interface AppComponentStub : BaseAppComponent

