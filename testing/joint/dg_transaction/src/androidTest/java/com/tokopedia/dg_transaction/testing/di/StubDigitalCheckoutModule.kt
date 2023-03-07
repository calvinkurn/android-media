package com.tokopedia.dg_transaction.testing.di

import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse
import com.tokopedia.dg_transaction.testing.response.rest.DigitalRestCheckoutMockResponse
import com.tokopedia.dg_transaction.testing.response.rest.RestRepositoryStub
import com.tokopedia.digital_checkout.di.DigitalCartCheckoutQualifier
import com.tokopedia.digital_checkout.di.DigitalCheckoutScope
import com.tokopedia.digital_checkout.utils.analytics.DigitalAnalytics
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor


/** Changes from the original:
 * - Does not include akamai interceptor injector (deleted, due to unexpected error)
 * - add provideRestRepositoryStub() to provide DigitalCheckoutUseCase mock response*/

@Module
class StubDigitalCheckoutModule {

    @DigitalCheckoutScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @DigitalCheckoutScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DigitalCheckoutScope
    @DigitalCartCheckoutQualifier
    fun provideDigitalCheckoutInterceptor(digitalInterceptor: DigitalInterceptor
    ): ArrayList<Interceptor> {
        val listInterceptor = arrayListOf<Interceptor>()
        listInterceptor.add(digitalInterceptor)
        listInterceptor.add(ErrorResponseInterceptor(TkpdDigitalResponse.DigitalErrorResponse::class.java))
        return listInterceptor
    }

    @Provides
    @DigitalCheckoutScope
    fun provideRestRepositoryStub(): RestRepositoryStub {
        return RestRepositoryStub().apply {
            responses = DigitalRestCheckoutMockResponse().getMockResponse()
        }
    }

    @Provides
    @DigitalCheckoutScope
    @DigitalCartCheckoutQualifier
    fun provideCheckoutRestRepository(repositoryStub: RestRepositoryStub): RestRepository = repositoryStub

    @DigitalCheckoutScope
    @Provides
    fun provideDigitalAnalytics(): DigitalAnalytics = DigitalAnalytics()

    @DigitalCheckoutScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()
}