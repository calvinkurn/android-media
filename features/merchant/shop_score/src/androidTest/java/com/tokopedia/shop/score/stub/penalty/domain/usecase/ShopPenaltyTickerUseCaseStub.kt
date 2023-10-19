package com.tokopedia.shop.score.stub.penalty.domain.usecase

import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltyTickerResponse
import com.tokopedia.shop.score.penalty.domain.usecase.ShopPenaltyTickerUseCase
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub

class ShopPenaltyTickerUseCaseStub(
    private val graphqlRepositoryStub: GraphqlRepositoryStub
) : ShopPenaltyTickerUseCase(graphqlRepositoryStub) {

    var responseStub = ShopPenaltyTickerResponse()
        set(value) {
            graphqlRepositoryStub.createMapResult(responseStub::class.java, value)
            field = value
        }

    override suspend fun executeOnBackground(): ShopPenaltyTickerResponse {
        return responseStub
    }
}
