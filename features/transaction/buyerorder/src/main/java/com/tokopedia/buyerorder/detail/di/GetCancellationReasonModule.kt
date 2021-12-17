package com.tokopedia.buyerorder.detail.di

import com.tokopedia.buyerorder.detail.data.getcancellationreason.BuyerGetCancellationReasonData
import com.tokopedia.buyerorder.detail.data.instantcancellation.BuyerInstantCancelData
import com.tokopedia.buyerorder.detail.data.requestcancel.BuyerRequestCancelData
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
class GetCancellationReasonModule {

    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @OrderDetailScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @OrderDetailScope
    @Provides
    fun provideGetCancellationReasonUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<BuyerGetCancellationReasonData.Data> = GraphqlUseCase(graphqlRepository)

    @OrderDetailScope
    @Provides
    fun provideBuyerInstantCancelUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<BuyerInstantCancelData.Data> = GraphqlUseCase(graphqlRepository)

    @OrderDetailScope
    @Provides
    fun provideBuyerRequestCancelUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<BuyerRequestCancelData.Data> = GraphqlUseCase(graphqlRepository)
}