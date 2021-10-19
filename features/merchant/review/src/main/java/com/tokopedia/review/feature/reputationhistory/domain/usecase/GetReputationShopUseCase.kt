package com.tokopedia.review.feature.reputationhistory.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationShopResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetReputationShopUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository
): UseCase<ReputationShopResponse>() {

    private var params = mapOf<String, Any>()

    fun setParams(shopId: Long) {
        params = RequestParams.create().apply {
            putLong(SHOP_IDS_PARAM, shopId)
        }.parameters
    }

    override suspend fun executeOnBackground(): ReputationShopResponse {
        val gqlRequest = GraphqlRequest(REPUTATION_SHOP_QUERY, ReputationShopResponse::class.java, params)
        val gqlResponse = gqlRepository.response(listOf(gqlRequest))

        val errors = gqlResponse.getError(ReputationShopResponse::class.java)
        if (errors.isNullOrEmpty()) {
            return gqlResponse.getData(ReputationShopResponse::class.java)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        const val SHOP_IDS_PARAM = "shop_ids"

        val REPUTATION_SHOP_QUERY = """
            query reputation_shops(${'$'}shop_ids: Int!) {
              reputation_shops(shop_ids: [${'$'}shop_ids]) {
                  badge
                  badge_hd
                  score
                  score_map
                }
            }
        """.trimIndent()
    }
}