package com.tokopedia.shop.score.penalty.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailParam
import com.tokopedia.shop.score.penalty.domain.response.ShopScorePenaltyDetailResponse
import javax.inject.Inject

private const val QUERY = """
    query ShopScoreNotYetDeductedQuery(${'$'}input: ShopScorePenaltyDetailParam!) {
       shopScorePenaltyDetail(input: ${'$'}input) {
          result {
              shopPenaltyID
          }
          error {
              message
          }
       }
    }
"""

@GqlQuery("ShopScoreNotYetDeductedQuery", QUERY)
class GetNotYetDeductedPenaltyUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
): GraphqlUseCase<ShopScorePenaltyDetailResponse>(gqlRepository){

    init {
        setTypeClass(ShopScorePenaltyDetailResponse::class.java)
        setGraphqlQuery(ShopScoreNotYetDeductedQuery())
    }

    suspend fun execute(
        startDate: String,
        endDate: String
    ): List<ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail.Result> {
        val params = createParams(
            ShopScorePenaltyDetailParam(
                startDate = startDate,
                endDate = endDate,
                total = MAX_ITEMS_PER_HIT,
                status = ShopScoreConstant.STATUS_NOT_YET_DEDUCTED,
            )
        )
        setRequestParams(params)
        return executeOnBackground().shopScorePenaltyDetail.result
    }

    companion object {

        private const val SHOP_SCORE_PENALTY_DETAIL_INPUT = "input"
        private const val MAX_ITEMS_PER_HIT = 1000

        @JvmStatic
        fun createParams(shopScorePenaltyDetailParam: ShopScorePenaltyDetailParam): Map<String, Any> =
            mapOf(SHOP_SCORE_PENALTY_DETAIL_INPUT to shopScorePenaltyDetailParam)


    }

}
