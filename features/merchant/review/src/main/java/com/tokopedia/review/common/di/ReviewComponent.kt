package com.tokopedia.review.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import retrofit2.Retrofit

/**
 * @author by Rafli Syam on 02-27-2020
 */
@Component(modules = [ReviewModule::class], dependencies = [BaseAppComponent::class])
@ReviewScope
interface ReviewComponent {

    @ApplicationContext
    fun getContext(): Context

    fun getRetrofitBuilder(): Retrofit.Builder

    fun getGraphqlRepository(): GraphqlRepository

    fun getMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase

    fun userSession(): UserSessionInterface

    fun coroutineDispatcher(): CoroutineDispatchers
}