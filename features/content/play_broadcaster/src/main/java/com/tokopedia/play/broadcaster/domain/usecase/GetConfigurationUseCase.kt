package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.GetBroadcasterShopConfigResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by mzennis on 14/06/20.
 */
class GetConfigurationUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<GetBroadcasterShopConfigResponse.GetBroadcasterShopConfig>() {

    private val query = """
            query getConfig(${'$'}shopId: String!) {
              broadcasterGetShopConfig(shopID: ${'$'}shopId) {
                streamAllowed
                config
              }
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): GetBroadcasterShopConfigResponse.GetBroadcasterShopConfig {
        val gqlRequest = GraphqlRequest(query, GetBroadcasterShopConfigResponse.GetBroadcasterShopConfigData::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<GetBroadcasterShopConfigResponse.GetBroadcasterShopConfigData>(GetBroadcasterShopConfigResponse.GetBroadcasterShopConfigData::class.java)
        response?.data?.config?.let {
            return it
        }
        throw MessageErrorException("Terjadi kesalahan pada server") // TODO("replace with default error message")
    }

    companion object {

        private const val PARAMS_SHOP_ID = "shopId"

        fun createParams(
                shopId: String
        ): Map<String, Any> = mapOf(
                PARAMS_SHOP_ID to shopId
        )
    }

}