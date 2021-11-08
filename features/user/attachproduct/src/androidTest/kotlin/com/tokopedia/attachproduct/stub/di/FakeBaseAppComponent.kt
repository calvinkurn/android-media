package com.tokopedia.attachproduct.stub.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.attachproduct.AttachProductTest
import dagger.Component

@ApplicationScope
@Component(modules = [FakeAppModule::class])
interface FakeBaseAppComponent : BaseAppComponent {
    fun inject(dep: AttachProductTest)
}