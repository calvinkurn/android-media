package com.tokopedia.universal_sharing.stub.di.base

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.universal_sharing.stub.data.repository.FakeGraphqlRepository
import dagger.Component

@ApplicationScope
@Component(modules = [UniversalSharingFakeAppModule::class])
interface UniversalSharingFakeBaseAppComponent : BaseAppComponent {

    @ApplicationContext
    fun fakeGraphqlRepository(): FakeGraphqlRepository
}
