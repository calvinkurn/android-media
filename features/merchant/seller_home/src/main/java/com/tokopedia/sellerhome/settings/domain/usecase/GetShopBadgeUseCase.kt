package com.tokopedia.sellerhome.settings.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.settings.domain.entity.ReputationShop
import com.tokopedia.sellerhome.settings.domain.entity.ReputationShopsResult
import com.tokopedia.sellerhome.settings.domain.mapReputationToBadgeUrl
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopBadgeUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : UseCase<String>() {

    companion object {
        const val QUERY = "query getShopBadge(\$shopIds: [Int!]!){\n" +
                "     reputation_shops(shop_ids: \$shopIds) {\n" +
                "         badge\n" +
                "         badge_hd\n" +
                "         score\n" +
                "         score_map\n" +
                "     }\n" +
                " }"

        private const val SHOP_ID_KEY = "shopIds"

        fun createRequestParams(shopId: Int) = HashMap<String, Any>().apply {
            put(SHOP_ID_KEY, listOf(shopId))
        }
    }

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): String {
        val gqlRequest = GraphqlRequest(QUERY, ReputationShopsResult::class.java, params)
        val gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(ReputationShop::class.java)
        if (errors.isNullOrEmpty()) {
            val result: ReputationShopsResult = gqlResponse.getData(ReputationShopsResult::class.java)
            return result.mapReputationToBadgeUrl()
        } else throw MessageErrorException(errors.firstOrNull()?.message)
    }
}