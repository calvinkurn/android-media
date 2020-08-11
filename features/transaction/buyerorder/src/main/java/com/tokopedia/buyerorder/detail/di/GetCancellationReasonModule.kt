package com.tokopedia.buyerorder.detail.di

import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.common.BuyerProductionDispatcherProvider
import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.list.di.OrderListModuleScope
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 12/06/20.
 */

@Module
@OrderListModuleScope
class GetCancellationReasonModule {
    @OrderListModuleScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @OrderListModuleScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @OrderListModuleScope
    @Provides
    fun provideBuyerDispatcherProvider(): BuyerDispatcherProvider = BuyerProductionDispatcherProvider()

    @OrderListModuleScope
    @Provides
    fun provideGetCancellationReasonUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<BuyerGetCancellationReasonData.Data> = GraphqlUseCase(graphqlRepository)
}