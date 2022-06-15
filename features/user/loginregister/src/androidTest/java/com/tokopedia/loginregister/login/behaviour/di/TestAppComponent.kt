package com.tokopedia.loginregister.login.behaviour.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface
import com.tokopedia.loginregister.login.behaviour.di.modules.AppModuleStub
import dagger.Component

@ApplicationScope
@Component(modules = [AppModuleStub::class])
interface TestAppComponent : BaseAppComponent {
    fun fakeGraphql(): GraphqlUseCaseInterface
}