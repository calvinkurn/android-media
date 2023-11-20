package com.tokopedia.shop.score.stub.penalty.domain.usecase

import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.shop.score.penalty.domain.usecase.GetOngoingPenaltyDateUseCase
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub

class GetOngoingPenaltyDateUseCaseStub(
    private val graphqlRepositoryStub: GraphqlRepositoryStub
) : GetOngoingPenaltyDateUseCase(graphqlRepositoryStub) {

    var responseStub = ShopScorePenaltyDetailResponse()
        set(value) {
            graphqlRepositoryStub.createMapResult(responseStub::class.java, value)
            field = value
        }

    override suspend fun executeOnBackground(): ShopScorePenaltyDetailResponse {
        return responseStub
    }
}
