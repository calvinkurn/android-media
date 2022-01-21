package com.tokopedia.shop.score.stub.performance.domain.usecase

import com.tokopedia.shop.score.performance.domain.model.ShopScoreWrapperResponse
import com.tokopedia.shop.score.performance.domain.usecase.GetShopPerformanceUseCase
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.shop.score.stub.performance.domain.response.ShopScoreResponseStub

class GetShopPerformanceUseCaseStub(
    private val graphqlRepositoryStub: GraphqlRepositoryStub
) : GetShopPerformanceUseCase(graphqlRepositoryStub) {

    var responseStub = ShopScoreResponseStub()
        set(value) {
            graphqlRepositoryStub.createMapResult(responseStub::class.java, value)
            field = value
        }

    override suspend fun executeOnBackground(): ShopScoreWrapperResponse {
        return ShopScoreWrapperResponse(
            shopScoreLevelResponse = responseStub.shopScoreLevel,
            shopScoreTooltipResponse = responseStub.shopLevelTooltipResponse,
            goldGetPMShopInfoResponse = responseStub.goldGetPMShopInfoResponse,
            goldGetPMOStatusResponse = responseStub.goldGetPMOSStatus.data,
            getRecommendationToolsResponse = responseStub.valuePropositionGetRecommendationTools
        )
    }
}