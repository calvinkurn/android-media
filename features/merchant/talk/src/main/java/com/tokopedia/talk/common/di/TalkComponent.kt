package com.tokopedia.talk.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.common.coroutine.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@Component(modules = [TalkModule::class], dependencies = [BaseAppComponent::class])
@TalkScope
interface TalkComponent {
    @ApplicationContext
    fun getContext(): Context
    fun graphqlRepository(): GraphqlRepository
    fun userSession(): UserSessionInterface
    fun coroutineDispatchers(): CoroutineDispatchers
}