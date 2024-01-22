package com.tokopedia.notifcenter.stub.di.base

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.notifcenter.stub.data.response.GqlResponseStub
import dagger.Component

@ApplicationScope
@Component(modules = [NotificationFakeAppModule::class])
interface NotificationFakeBaseAppComponent : BaseAppComponent {
    @ApplicationContext
    fun getGqlResponseStub(): GqlResponseStub
}
