package com.tokopedia.review.stub.reviewcommon.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.common.di.ReviewScope
import com.tokopedia.review.stub.common.di.component.BaseAppComponentStub
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@Component(dependencies = [BaseAppComponentStub::class])
@ReviewScope
interface ReviewComponentStub {
    @ApplicationContext
    fun getContext(): Context

    fun getGraphqlRepository(): GraphqlRepository

    fun userSession(): UserSessionInterface

    fun coroutineDispatcher(): CoroutineDispatchers
}