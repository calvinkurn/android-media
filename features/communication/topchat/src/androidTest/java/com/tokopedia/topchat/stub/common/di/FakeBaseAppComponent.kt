package com.tokopedia.topchat.stub.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.topchat.stub.common.di.module.FakeAppModule
import dagger.Component

@ApplicationScope
@Component(modules = [FakeAppModule::class])
interface FakeBaseAppComponent : BaseAppComponent