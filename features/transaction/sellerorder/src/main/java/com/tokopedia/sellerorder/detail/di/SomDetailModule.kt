package com.tokopedia.sellerorder.detail.di

import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerorder.common.domain.model.SomAcceptOrderResponse
import com.tokopedia.sellerorder.common.domain.model.SomEditRefNumResponse
import com.tokopedia.sellerorder.common.domain.model.SomRejectOrderResponse
import com.tokopedia.sellerorder.detail.data.model.SetDeliveredResponse
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.detail.data.model.SomReasonRejectData
import dagger.Module
import dagger.Provides

/**
 * Created by fwidjaja on 10/05/20.
 */
@Module
class SomDetailModule {

    @SomDetailScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @SomDetailScope
    @Provides
    fun provideSomGetDetailOrderUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomDetailOrder.Data> = GraphqlUseCase(graphqlRepository)

    @SomDetailScope
    @Provides
    fun provideSomAcceptOrderUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomAcceptOrderResponse.Data> = GraphqlUseCase(graphqlRepository)

    @SomDetailScope
    @Provides
    fun provideSomReasonRejectUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomReasonRejectData.Data> = GraphqlUseCase(graphqlRepository)

    @SomDetailScope
    @Provides
    fun provideSomRejectOrderUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomRejectOrderResponse.Data> = GraphqlUseCase(graphqlRepository)

    @SomDetailScope
    @Provides
    fun provideSomEditRefNumUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomEditRefNumResponse.Data> = GraphqlUseCase(graphqlRepository)

    @SomDetailScope
    @Provides
    fun provideSomSetDeliveredUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SetDeliveredResponse> = GraphqlUseCase(graphqlRepository)
}