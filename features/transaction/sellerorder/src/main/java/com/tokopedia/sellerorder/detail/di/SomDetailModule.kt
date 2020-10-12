package com.tokopedia.sellerorder.detail.di

import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.SomProductionDispatcherProvider
import com.tokopedia.sellerorder.detail.data.model.*
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 10/05/20.
 */
@Module
@SomDetailScope
class SomDetailModule {
    @SomDetailScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @SomDetailScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @SomDetailScope
    @Provides
    fun provideSomDispatcherProvider(): SomDispatcherProvider = SomProductionDispatcherProvider()

    @SomDetailScope
    @Provides
    fun provideSomGetDetailOrderUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomDetailOrder.Data> = GraphqlUseCase(graphqlRepository)

    @SomDetailScope
    @Provides
    fun provideSomAcceptOrderUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomAcceptOrder.Data> = GraphqlUseCase(graphqlRepository)

    @SomDetailScope
    @Provides
    fun provideSomReasonRejectUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomReasonRejectData.Data> = GraphqlUseCase(graphqlRepository)

    @SomDetailScope
    @Provides
    fun provideSomRejectOrderUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomRejectOrder.Data> = GraphqlUseCase(graphqlRepository)

    @SomDetailScope
    @Provides
    fun provideSomEditRefNumUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomEditAwbResponse.Data> = GraphqlUseCase(graphqlRepository)

    @SomDetailScope
    @Provides
    fun provideSomSetDeliveredUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SetDeliveredResponse> = GraphqlUseCase(graphqlRepository)
}