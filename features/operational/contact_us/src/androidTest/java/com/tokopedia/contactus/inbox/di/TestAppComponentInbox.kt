package com.tokopedia.contactus.inbox.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component

@ApplicationScope
@Component(modules = [FakeAppInboxModule::class])
interface TestAppComponentInbox : BaseAppComponent {

    @ApplicationContext
    fun fakeGraphqlRepository(): GraphqlRepository
}
