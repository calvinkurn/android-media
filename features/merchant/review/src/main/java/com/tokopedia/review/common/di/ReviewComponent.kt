package com.tokopedia.review.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.review.common.coroutine.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@Component(modules = [ReviewModule::class], dependencies = [BaseAppComponent::class])
@ReviewScope
interface ReviewComponent {
    @ApplicationContext
    fun getContext(): Context
    fun graphqlRepository(): GraphqlRepository
    fun userSession(): UserSessionInterface
    fun coroutineDispatchers(): CoroutineDispatchers
}