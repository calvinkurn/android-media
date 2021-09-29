package com.tokopedia.review.feature.reputationhistory.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationPenaltyAndRewardResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetReputationPenaltyRewardUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository
): UseCase<ReputationPenaltyAndRewardResponse.Data>() {

    private var params = mapOf<String, Any>()

    fun setParams(shopId: Long, page: Int, startDate: String, endDate: String) {
        params = RequestParams.create().apply {
            putLong(SHOP_ID_PARAM, shopId)
            putInt(PAGE_PARAM, page)
            putInt(TOTAL_PARAM, TOTAL_DEFAULT)
            putString(START_DATE_PARAM, startDate)
            putString(END_DATE_PARAM, endDate)
        }.parameters
    }

    override suspend fun executeOnBackground(): ReputationPenaltyAndRewardResponse.Data {
        val gqlRequest = GraphqlRequest(
            REPUTATION_PENALTY_REWARD_QUERY,
            ReputationPenaltyAndRewardResponse.Data::class.java,
            params
        )
        val gqlResponse = gqlRepository.response(listOf(gqlRequest))

        val errors = gqlResponse.getError(ReputationPenaltyAndRewardResponse.Data::class.java)
        if (errors.isNullOrEmpty()) {
            return gqlResponse.getData(ReputationPenaltyAndRewardResponse.Data::class.java)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        const val SHOP_ID_PARAM = "shop_id"
        const val START_DATE_PARAM = "start_date"
        const val END_DATE_PARAM = "end_date"
        const val PAGE_PARAM = "page"
        const val TOTAL_PARAM = "total"
        const val TOTAL_DEFAULT = 10

        val REPUTATION_PENALTY_REWARD_QUERY = """
            query reputation_penalty_and_reward(${'$'}shop_id: Int!, ${'$'}page: Int!, ${'$'}total: Int!, ${'$'}start_date: String!, ${'$'}end_date: String!){
                reputation_penalty_and_reward(shop_id: ${'$'}shop_id, page: ${'$'}page, total: ${'$'}total, start_date: ${'$'}start_date, end_date: ${'$'}end_date){
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