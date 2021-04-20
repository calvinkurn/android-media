package com.tokopedia.orderhistory.stub.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.di.OrderHistoryScope
import com.tokopedia.orderhistory.stub.common.GraphqlUseCaseStub
import com.tokopedia.orderhistory.stub.usecase.GetProductOrderHistoryUseCaseStub
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import dagger.Module
import dagger.Provides


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
            gqlUseCase: GraphqlUseCaseStub<ChatHistoryProductResponse>,
            dispatchers: CoroutineDispatchers
    ): GetProductOrderHistoryUseCaseStub {
        return GetProductOrderHistoryUseCaseStub(gqlUseCase, dispatchers)
    }

}