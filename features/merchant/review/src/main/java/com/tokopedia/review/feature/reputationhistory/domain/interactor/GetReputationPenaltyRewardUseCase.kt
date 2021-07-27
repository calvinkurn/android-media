package com.tokopedia.review.feature.reputationhistory.domain.interactor

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationPenaltyRewardResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetReputationPenaltyRewardUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository
): UseCase<ReputationPenaltyRewardResponse>() {

    private var params = mapOf<String, Any>()

    fun setParams(shopId: String, page: Long, startDate: String, endDate: String) {
        params = RequestParams.create().apply {
            putString(SHOP_ID_PARAM, shopId)
            putLong(PAGE_PARAM, page)
            putString(START_DATE_PARAM, startDate)
            putString(END_DATE_PARAM, endDate)
        }.parameters
    }

    override suspend fun executeOnBackground(): ReputationPenaltyRewardResponse {
        val gqlRequest = GraphqlRequest(
            REPUTATION_PENALTY_REWARD_QUERY,
            ReputationPenaltyRewardResponse::class.java,
            params
        )
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(ReputationPenaltyRewardResponse::class.java)
        if (errors.isNullOrEmpty()) {
            return gqlResponse.getData(ReputationPenaltyRewardResponse::class.java)
        } else {
            throw RuntimeException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        const val SHOP_ID_PARAM = "shop_id"
        const val START_DATE_PARAM = "start_date"
        const val END_DATE_PARAM = "end_date"
        const val PAGE_PARAM = "page"

        val REPUTATION_PENALTY_REWARD_QUERY = """
            query reputation_penalty_and_reward(${'$'}shop_id: Int!, ${'$'}page: Int!, ${'$'}start_date: String!, ${'$'}end_date: String!){
                reputation_penalty_and_reward(shop_id: ${'$'}shop_id, page: ${'$'}page, total: 10, start_date: ${'$'}start_date, end_date: ${'$'}end_date){
                      list {
                        id
                        invoice_ref_num
                        score
                        information
                        time_fmt
                      }
                      page {
                        prev
                        next
                      }
                    }
                  }
        """.trimIndent()
    }

}