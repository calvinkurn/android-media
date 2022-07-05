package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.mapper.ShopInfoTickerMapper
import com.tokopedia.sellerhomecommon.domain.model.GetShopInfoTickerResponse
import com.tokopedia.sellerhomecommon.presentation.model.TickerItemUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 25/04/22.
 */

@GqlQuery("GetShopInfoTickerGqlQuery", GetShopInfoTickerUseCase.QUERY)
class GetShopInfoTickerUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    mapper: ShopInfoTickerMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetShopInfoTickerResponse, List<TickerItemUiModel>>(
    gqlRepository, mapper, dispatchers, GetShopInfoTickerGqlQuery()
) {

    override val classType: Class<GetShopInfoTickerResponse>
        get() = GetShopInfoTickerResponse::class.java

    override suspend fun executeOnBackground(): List<TickerItemUiModel> {
        val gqlRequest = GraphqlRequest(graphqlQuery, classType, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val errors = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetShopInfoTickerResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            return mapper.mapRemoteDataToUiData(data, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message)
        }
    }

    companion object {
        internal const val QUERY = """
            query shopInfoByID(${'$'}shopId: Int!) {
              shopInfoByID(input: {shopIDs: [${'$'}shopId], fields: ["status"], domain: "", source: "sellerapp"}) {
                result {
                  statusInfo {
                    shopStatus
                    statusTitle
                    statusMessage
                    tickerType
                  }
                }
              }
            }
        """
        private const val SHOP_ID = "shopId"

        fun createParams(shopId: String): RequestParams {
            return RequestParams.create().apply {
                putLong(SHOP_ID, shopId.toLongOrZero())
            }
        }
    }
}