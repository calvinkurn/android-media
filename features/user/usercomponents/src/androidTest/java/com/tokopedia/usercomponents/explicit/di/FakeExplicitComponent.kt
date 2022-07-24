package com.tokopedia.usercomponents.explicit.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usercomponents.common.stub.di.FakeAppModule
import dagger.Component

@ApplicationScope
@Component(modules = [
    FakeAppModule::class,
    FakeExplicitModule::class,
])
interface FakeExplicitComponent : BaseAppComponent {

    @ApplicationContext
    fun repo(): GraphqlRepository
}