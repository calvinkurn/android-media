package com.tokopedia.buyerorderdetail.stub.detail.di.module

import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailScope
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

@Module
class BuyerOrderDetailUseCaseModuleStub {
    @BuyerOrderDetailScope
    @Provides
    fun provideGetBuyerOrderDetailGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetBuyerOrderDetailResponse.Data> {
        return GraphqlUseCase(graphqlRepository)
    }

    @BuyerOrderDetailScope
    @Provides
    fun provideFinishOrderGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<FinishOrderResponse.Data> {
        return GraphqlUseCase(graphqlRepository)
    }

    @BuyerOrderDetailScope
    @Provides
    fun provideAddToCartMultiGraphqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<AtcMultiData> {
        return GraphqlUseCase(graphqlRepository)
    }
}