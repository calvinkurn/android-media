package com.tokopedia.review.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import retrofit2.Retrofit

@Component(modules = [ReviewInboxModule::class], dependencies = [BaseAppComponent::class])
@ReviewInboxScope
interface ReviewInboxComponent {

    @ApplicationContext
    fun getContext(): Context

    fun getRetrofitBuilder(): Retrofit.Builder

    fun getGraphqlRepository(): GraphqlRepository

    fun getMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase

    fun userSession(): UserSessionInterface

    fun coroutineDispatcher(): CoroutineDispatchers
}