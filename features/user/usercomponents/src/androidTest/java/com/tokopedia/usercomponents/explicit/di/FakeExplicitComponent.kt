package com.tokopedia.usercomponents.explicit.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.common.stub.di.FakeAppModule
import com.tokopedia.usercomponents.explicit.di.module.FakeExplicitModule
import com.tokopedia.usercomponents.explicit.fake_view.ExplicitDebugFragment
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        FakeAppModule::class,
        FakeExplicitModule::class,
    ]
)
interface FakeExplicitComponent : BaseAppComponent {

    @ApplicationContext
    fun repository(): GraphqlRepository

    fun userSession(): UserSessionInterface

    fun inject(fragment: ExplicitDebugFragment)
}