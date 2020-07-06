package com.tokopedia.buyerorder.unifiedhistory.list.di

import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.common.BuyerProductionDispatcherProvider
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 04/07/20.
 */

@Module
@UohListScope
class UohListModule {
    @UohListScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @UohListScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @UohListScope
    @Provides
    fun provideBuyerDispatcherProvider(): BuyerDispatcherProvider = BuyerProductionDispatcherProvider()

    @UohListScope
    @Provides
    fun provideUohGetUnifiedOrderHistoryUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<UohListOrder.Data> = GraphqlUseCase(graphqlRepository)
}