package com.tokopedia.sellerorder.list.di

import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.SomProductionDispatcherProvider
import com.tokopedia.sellerorder.list.data.model.SomListAllFilter
import com.tokopedia.sellerorder.list.data.model.SomListFilter
import com.tokopedia.sellerorder.list.data.model.SomListOrder
import com.tokopedia.sellerorder.list.data.model.SomListTicker
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 06/05/20.
 */

@Module
@SomListScope
class SomListModule {
    @SomListScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @SomListScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @SomListScope
    @Provides
    fun provideSomDispatcherProvider(): SomDispatcherProvider = SomProductionDispatcherProvider()

    @SomListScope
    @Provides
    fun provideSomGetTickerListUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomListTicker.Data> = GraphqlUseCase(graphqlRepository)

    @SomListScope
    @Provides
    fun provideSomGetFilterStatusListUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomListAllFilter.Data> = GraphqlUseCase(graphqlRepository)

    @SomListScope
    @Provides
    fun provideSomGetFilterListUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomListFilter.Data> = GraphqlUseCase(graphqlRepository)

    @SomListScope
    @Provides
    fun provideSomGetOrderListUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomListOrder.Data> = GraphqlUseCase(graphqlRepository)

    @SomListScope
    @Provides
    fun provideSomGetAllFilterUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomListAllFilter.Data> = GraphqlUseCase(graphqlRepository)
}