package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.model.GetShopOperationalHourResponse
import com.tokopedia.sellerhome.domain.model.ShopOperationalHourResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetShopOperationalHourGqlQuery", GetShopOperationalHourUseCase.QUERY)
class GetShopOperationalHourUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
): GraphqlUseCase<GetShopOperationalHourResponse>(gqlRepository) {

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GetShopOperationalHourGqlQuery())
        setTypeClass(GetShopOperationalHourResponse::class.java)
    }

    suspend fun execute(shopId: String): ShopOperationalHourResponse {
        val requestParams = RequestParams.create().apply {
            putString(PARAM_SHOP_ID, shopId)
            putInt(PARAM_TYPE, FEATURE_TYPE_ID)
        }
        setRequestParams(requestParams.parameters)

        val response = executeOnBackground()
        val shopOperationalHour = response.data
        val errorMessage = shopOperationalHour.error?.message

        return when {
            errorMessage.isNullOrBlank() -> shopOperationalHour
            !errorMessage.isNullOrBlank() -> throw MessageErrorException(errorMessage)
            else -> throw MessageErrorException(ERROR_MESSAGE)
        }
    }

    companion object {
        const val QUERY = """
            query getShopOperationalHourStatus(${'$'}shopID: String!, ${'$'}type: Int!) {
              getShopOperationalHourStatus(shopID: ${'$'}shopID, type: ${'$'}type) {
                statusActive
                startTime
                endTime
                error {
                  message
                }
              }
            }
        """
        private const val FEATURE_TYPE_ID = 1

        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_TYPE = "type"

        private const val ERROR_MESSAGE = "Failed to get shop operational hour"
    }
}