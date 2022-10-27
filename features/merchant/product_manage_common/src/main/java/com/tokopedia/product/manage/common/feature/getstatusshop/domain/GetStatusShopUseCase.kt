package com.tokopedia.product.manage.common.feature.getstatusshop.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.feature.getstatusshop.data.model.StatusInfo
import com.tokopedia.product.manage.common.feature.getstatusshop.data.response.ShopStatusResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

@GqlQuery("GetShopStatusGqlQuery", GetStatusShopUseCase.QUERY)
class GetStatusShopUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository
) : UseCase<StatusInfo>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): StatusInfo {
        val gqlRequest = GraphqlRequest(
            GetShopStatusGqlQuery(),
            ShopStatusResponse::class.java,
            params.parameters
        )

        val cacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CACHE_FIRST)
            .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`()).build()

        val gqlResponse = gqlRepository.response(listOf(gqlRequest), cacheStrategy)

        val gqlErrors = gqlResponse.getError(ShopStatusResponse::class.java)
        if (gqlErrors.isNullOrEmpty()) {
            val data =
                gqlResponse.getData<ShopStatusResponse>(ShopStatusResponse::class.java)
            val statusInfo: StatusInfo? =
                data?.shopInfoById?.result?.firstOrNull()?.statusInfo
            if (statusInfo != null) {
                return statusInfo
            } else {
                throw RuntimeException()
            }
        } else {
            throw MessageErrorException(gqlErrors.firstOrNull()?.message.orEmpty())
        }
    }

    companion object {
        const val QUERY = """
            query shopInfoByID (${'$'}shopID: Int!){
                shopInfoByID(input:{shopIDs: [${'$'}shopID], fields:["status"]}) {
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

        private const val PARAM_SHOP_ID = "shopID"

        fun createRequestParams(shopId: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putInt(PARAM_SHOP_ID, shopId)
            return requestParams
        }
    }
}
