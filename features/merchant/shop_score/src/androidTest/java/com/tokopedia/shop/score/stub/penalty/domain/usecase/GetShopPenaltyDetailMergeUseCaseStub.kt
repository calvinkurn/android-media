package com.tokopedia.shop.score.stub.penalty.domain.usecase

import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltyDetailMergeResponse
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltySummaryTypeWrapper
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import com.tokopedia.shop.score.penalty.domain.usecase.GetOngoingPenaltyDateUseCase
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCase
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub

class GetShopPenaltyDetailMergeUseCaseStub(
    private val graphqlRepositoryStub: GraphqlRepositoryStub,
    getOngoingPenaltyDateUseCase: GetOngoingPenaltyDateUseCase
) : GetShopPenaltyDetailMergeUseCase(graphqlRepositoryStub, getOngoingPenaltyDateUseCase) {

    var responseStub = ShopPenaltyDetailMergeResponse()
        set(value) {
            graphqlRepositoryStub.createMapResult(responseStub::class.java, value)
            field = value
        }

    override suspend fun executeOnBackground(): Pair<ShopPenaltySummaryTypeWrapper, ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail> {
        val penaltySummaryResponse = responseStub.shopScorePenaltySummary
        val penaltyTypesResponse = responseStub.shopScorePenaltyTypes
        val shopScorePenaltySummaryWrapper = ShopPenaltySummaryTypeWrapper(
            shopScorePenaltySummaryResponse = penaltySummaryResponse,
            shopScorePenaltyTypesResponse = penaltyTypesResponse
        )
        val penaltyDetailResponse = responseStub.shopScorePenaltyDetail

        return shopScorePenaltySummaryWrapper to penaltyDetailResponse
    }
}
