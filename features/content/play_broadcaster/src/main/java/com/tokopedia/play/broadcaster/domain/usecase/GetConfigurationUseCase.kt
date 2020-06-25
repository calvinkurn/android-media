package com.tokopedia.play.broadcaster.domain.usecase

import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.domain.model.Config
import com.tokopedia.play.broadcaster.domain.model.GetBroadcasterShopConfigResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by mzennis on 14/06/20.
 */
class GetConfigurationUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : UseCase<Config>() {

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
        val gqlRequest = GraphqlRequest(query, GetBroadcasterShopConfigResponse.GetBroadcasterShopConfigData::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val response = gqlResponse.getData<GetBroadcasterShopConfigResponse.GetBroadcasterShopConfigData>(GetBroadcasterShopConfigResponse.GetBroadcasterShopConfigData::class.java)
        response?.data?.config?.let { shopConfig -> return mapConfiguration(shopConfig) }
        throw MessageErrorException("Ada sedikit kendala pada sistem.")
    }

    private fun mapConfiguration(shopConfig: GetBroadcasterShopConfigResponse.GetBroadcasterShopConfig): Config {
        return try {
            val config= gson.fromJson(shopConfig.config, Config::class.java)
            config.copy(streamAllowed = shopConfig.streamAllowed)
        } catch (e: Exception) {
            if (!GlobalConfig.DEBUG) {
                Crashlytics.log(0, TAG, e.localizedMessage)
            }
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