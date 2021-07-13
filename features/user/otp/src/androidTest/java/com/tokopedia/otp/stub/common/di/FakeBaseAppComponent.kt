package com.tokopedia.otp.stub.common.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [FakeAppModule::class])
interface FakeBaseAppComponent : BaseAppComponent