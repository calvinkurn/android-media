package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.model.GetShopClosedInfoResponse
import com.tokopedia.sellerhome.domain.model.ShopInfoResultResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetShopInfoByIdUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<GetShopClosedInfoResponse>(gqlRepository) {

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private const val ERROR_MESSAGE = "Failed to get shop closed info"

        private val QUERY = """
            query shopInfoByID(${'$'}shopID: Int!) {
              shopInfoByID(input: {shopIDs: [${'$'}shopID], fields: ["core","closed_info","shop-snippet","status"]}) {
                result {
                  shopCore{
                    url
                  }
                  closedInfo {
                    detail {
                      startDate
                      endDate
                      status
                    }
                  }
                  statusInfo {
                    shopStatus
                  }
                  shopSnippetURL
                }
                error {
                  message
                }
              }
            }
        """.trimIndent()
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(QUERY)
        setTypeClass(GetShopClosedInfoResponse::class.java)
    }

    suspend fun execute(shopId: Long): ShopInfoResultResponse {
        val requestParams = RequestParams.create().apply {
            putLong(PARAM_SHOP_ID, shopId)
        }
        setRequestParams(requestParams.parameters)

        val response = executeOnBackground()
        val shopInfo = response.data
        val result = shopInfo.result.firstOrNull()
        val errorMessage = shopInfo.error?.message

        return when {
            errorMessage.isNullOrBlank() && result != null -> result
            !errorMessage.isNullOrBlank() -> throw MessageErrorException(errorMessage)
            else -> throw MessageErrorException(ERROR_MESSAGE)
        }
    }
}