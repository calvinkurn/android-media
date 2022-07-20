package com.tokopedia.product.manage.stub.common.di.component

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface
import com.tokopedia.product.manage.stub.common.di.module.AppModuleStub
import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@ApplicationScope
@Component(modules = [AppModuleStub::class])
interface BaseAppComponentStub {

    @ApplicationContext
    fun getContext(): Context

    fun coroutineDispatchers(): CoroutineDispatchers

    fun graphqlRepository(): GraphqlRepository

    fun retrofitBuilder(): Retrofit.Builder

    fun gson(): Gson?

    fun provideAbstractionRouter(): AbstractionRouter?

    fun headerErrorResponseInterceptor(): HeaderErrorResponseInterceptor

    fun httpLoggingInterceptor(): HttpLoggingInterceptor

    fun getCacheManager(): CacheManager?

    fun graphqlInterface(): GraphqlUseCaseInterface

}