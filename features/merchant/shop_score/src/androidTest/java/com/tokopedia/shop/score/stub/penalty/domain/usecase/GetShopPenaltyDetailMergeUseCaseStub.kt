package com.tokopedia.shop.score.stub.penalty.domain.usecase

import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.common.format
import com.tokopedia.shop.score.common.getNowTimeStamp
import com.tokopedia.shop.score.common.getPastDaysPenaltyTimeStamp
import com.tokopedia.shop.score.penalty.domain.mapper.PenaltyMapper
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltyDetailMergeResponse
import com.tokopedia.shop.score.penalty.domain.response.ShopPenaltySummaryTypeWrapper
import com.tokopedia.shop.score.penalty.domain.usecase.GetShopPenaltyDetailMergeUseCase
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyDataWrapper
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub

class GetShopPenaltyDetailMergeUseCaseStub(
    private val graphqlRepositoryStub: GraphqlRepositoryStub,
    private val penaltyMapperStub: PenaltyMapper
) : GetShopPenaltyDetailMergeUseCase(graphqlRepositoryStub, penaltyMapperStub) {

    var responseStub = ShopPenaltyDetailMergeResponse()
        set(value) {
            graphqlRepositoryStub.createMapResult(responseStub::class.java, value)
            field = value
        }

    override suspend fun executeOnBackground(): PenaltyDataWrapper {
        val penaltySummaryResponse = responseStub.shopScorePenaltySummary
        val penaltyTypesResponse = responseStub.shopScorePenaltyTypes
        val shopScorePenaltySummaryWrapper = ShopPenaltySummaryTypeWrapper(
            shopScorePenaltySummaryResponse = penaltySummaryResponse,
            shopScorePenaltyTypesResponse = penaltyTypesResponse
        )
        val penaltyDetailResponse = responseStub.shopScorePenaltyDetail
        val startDate =
            format(getPastDaysPenaltyTimeStamp().time, ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM)
        val endDate = format(getNowTimeStamp(), ShopScoreConstant.PATTERN_PENALTY_DATE_PARAM)
        val sortBy = 0
        val typeId = 0

        return penaltyMapperStub.mapToPenaltyData(
            shopScorePenaltySummaryWrapper,
            penaltyDetailResponse,
            sortBy,
            typeId,
            Pair(startDate, endDate)
        )
    }
}