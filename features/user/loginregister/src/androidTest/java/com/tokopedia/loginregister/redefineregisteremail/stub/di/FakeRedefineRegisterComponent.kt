package com.tokopedia.loginregister.redefineregisteremail.stub.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component

@ApplicationScope
@Component(modules = [FakeRedefineRegisterModule::class])
interface FakeRedefineRegisterComponent : BaseAppComponent {
    @ApplicationContext
    fun repository(): GraphqlRepository
}
