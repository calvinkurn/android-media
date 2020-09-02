package com.tokopedia.play.broadcaster.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.Config
import com.tokopedia.play.broadcaster.domain.model.GetBroadcasterShopConfigResponse
import com.tokopedia.play.broadcaster.util.extension.sendCrashlyticsLog
import javax.inject.Inject

/**
 * Created by mzennis on 14/06/20.
 */
class GetConfigurationUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : BaseUseCase<Config>() {

    private val query = """
            query getConfig(${'$'}shopId: String!) {
              broadcasterGetShopConfig(shopID: ${'$'}shopId) {
                streamAllowed
                config
              }
            }
        """
    private val gson = Gson()

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): Config {
        val gqlResponse = configureGqlResponse(graphqlRepository, query, GetBroadcasterShopConfigResponse::class.java, params, GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<GetBroadcasterShopConfigResponse>(GetBroadcasterShopConfigResponse::class.java)
        return mapConfiguration(response.shopConfig.config)
                .copy(streamAllowed = response.shopConfig.streamAllowed)
    }

    private fun mapConfiguration(config: String): Config {
        return try {
            gson.fromJson(config, Config::class.java)
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
            Config()
        }
    }

    companion object {

        private const val TAG = "Play-GetConfigurationUseCase"
        private const val PARAMS_SHOP_ID = "shopId"

        fun createParams(
                shopId: String
        ): Map<String, Any> = mapOf(
                PARAMS_SHOP_ID to shopId
        )
    }
}