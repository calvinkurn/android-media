package com.tokopedia.orderhistory.stub.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.orderhistory.di.OrderHistoryScope
import com.tokopedia.orderhistory.stub.usecase.GetProductOrderHistoryUseCaseStub
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher


@Module
class OrderHistoryStubDependencyModule {

    // -- separator -- //

    @OrderHistoryScope
    @Provides
    fun provideGetProductOrderHistoryUseCase(
            stub: GetProductOrderHistoryUseCaseStub
    ): GetProductOrderHistoryUseCase = stub

    @OrderHistoryScope
    @Provides
    fun provideGetProductOrderHistoryUseCaseStub(
            repository: GraphqlRepository,
            dispatchers: CoroutineDispatcher
    ): GetProductOrderHistoryUseCaseStub {
        return GetProductOrderHistoryUseCaseStub(repository, dispatchers)
    }

}