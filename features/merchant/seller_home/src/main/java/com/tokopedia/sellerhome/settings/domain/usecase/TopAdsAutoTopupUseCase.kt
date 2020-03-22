package com.tokopedia.sellerhome.settings.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.sellerhome.settings.domain.entity.TopAdsAutoTopupDataModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class TopAdsAutoTopupUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<String>() {

    companion object {
        const val QUERY = "query GetTopAdsAutoTopup(\$shopId: String!) {\n" +
                "  topAdsAutoTopup(shop_id: \$shopId) {\n" +
                "    data {\n" +
                "      status\n" +
                "    }\n" +
                "    errors {\n" +
                "      Code\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val SHOP_ID_KEY = "shopId"

        fun createRequestParams(shopId: String) = HashMap<String, Any>().apply {
            put(SHOP_ID_KEY, shopId)
        }
    }

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): String {
        val gqlRequest = GraphqlRequest(QUERY, TopAdsAutoTopupDataModel::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val gqlError = gqlResponse.getError(TopAdsAutoTopupDataModel::class.java)
        if (gqlError.isNullOrEmpty()) {
            val topAdsAutoTopupResponse : TopAdsAutoTopupDataModel = gqlResponse.getData(TopAdsAutoTopupDataModel::class.java)
            val responseError = topAdsAutoTopupResponse.topAdsAutoTopup?.error
            if (responseError.isNullOrEmpty()) {
                topAdsAutoTopupResponse.topAdsAutoTopup?.autoTopupStatus?.status?.let {
                    return it
                }
            }
        }
        throw ResponseErrorException()
    }
}