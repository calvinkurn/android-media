package com.tokopedia.officialstore.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Component(modules = [OfficialStoreModule::class], dependencies = [BaseAppComponent::class])
@OfficialStoreScope
interface OfficialStoreComponent {
    @ApplicationContext
    fun getContext(): Context
    fun getMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase
    fun getDispatcherProvider(): CoroutineDispatchers
    fun httpLoggingInterceptor(): HttpLoggingInterceptor
    fun retrofitBuilder(): Retrofit.Builder
}
