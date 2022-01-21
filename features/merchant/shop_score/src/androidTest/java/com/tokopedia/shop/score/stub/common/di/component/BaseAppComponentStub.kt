package com.tokopedia.shop.score.stub.common.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.score.stub.common.di.module.AppModuleStub
import dagger.Component

@ApplicationScope
@Component(modules = [AppModuleStub::class])
interface BaseAppComponentStub {
    @ApplicationContext
    fun getContext(): Context

    fun coroutineDispatchers(): CoroutineDispatchers

    fun graphqlRepository(): GraphqlRepository
}