package com.tokopedia.shop.score.penalty.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailParam
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import javax.inject.Inject

private const val QUERY = """
    query OngoingPenaltyDateQuery(${'$'}input: ShopScorePenaltyDetailParam!) {
       shopScorePenaltyDetail(input: ${'$'}input) {
          startDate
          endDate
       }
    }
"""

@GqlQuery("OngoingPenaltyDateQuery", QUERY)
open class GetOngoingPenaltyDateUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<ShopScorePenaltyDetailResponse>(gqlRepository) {

    init {
        setTypeClass(ShopScorePenaltyDetailResponse::class.java)
        setGraphqlQuery(OngoingPenaltyDateQuery())
    }

    suspend fun execute(
        startDate: String,
        endDate: String,
        status: Int
    ): Pair<String, String> {
        val params = createParams(
            ShopScorePenaltyDetailParam(
                startDate = startDate,
                endDate = endDate,
                total = MAX_ITEMS_PER_HIT,
                status = status
            )
        )
        setRequestParams(params)
        return executeOnBackground().shopScorePenaltyDetail.let {
            it.startDate to it.endDate
        }
    }

    companion object {

        private const val SHOP_SCORE_PENALTY_DETAIL_INPUT = "input"
        private const val MAX_ITEMS_PER_HIT = 10

        @JvmStatic
        fun createParams(shopScorePenaltyDetailParam: ShopScorePenaltyDetailParam): Map<String, Any> =
            mapOf(SHOP_SCORE_PENALTY_DETAIL_INPUT to shopScorePenaltyDetailParam)
    }
}
