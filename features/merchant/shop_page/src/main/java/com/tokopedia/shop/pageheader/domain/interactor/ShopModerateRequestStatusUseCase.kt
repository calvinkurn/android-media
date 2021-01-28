package com.tokopedia.shop.pageheader.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import javax.inject.Inject

class ShopModerateRequestStatusUseCase @Inject constructor(
        private val gqlRepository : GraphqlRepository
) : GraphqlUseCase<ShopModerateRequestData>(gqlRepository) {

    override suspend fun executeOnBackground(): ShopModerateRequestData {
        val request = GraphqlRequest(QUERY, ShopModerateRequestData::class.java)
        val gqlResponse = gqlRepository.getReseponse(
                listOf(request),
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )
        return gqlResponse.getData<ShopModerateRequestData>(ShopModerateRequestData::class.java)
    }

    companion object {

        /**
         * Gql query to check shop moderate request status
         */
        private const val QUERY = "query shopModerateRequestStatus() {\n" +
                "  shopModerateRequestStatus {\n" +
                "    result {\n" +
                "      shopID\n" +
                "      status\n" +
                "    }\n" +
                "    error {\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}"

    }

}