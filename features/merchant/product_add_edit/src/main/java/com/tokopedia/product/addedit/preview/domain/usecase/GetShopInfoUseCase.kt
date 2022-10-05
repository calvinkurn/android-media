package com.tokopedia.product.addedit.preview.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.preview.data.model.responses.ShopInfoResponse
import com.tokopedia.product.addedit.preview.data.source.api.response.ShopInfo
import com.tokopedia.product.addedit.preview.domain.usecase.GetShopInfoUseCase.Companion.QUERY
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

import javax.inject.Inject

@GqlQuery("GetShopInfoGqlQuery", QUERY)
class GetShopInfoUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) : UseCase<ShopInfo>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopInfo {
        val graphqlRequest =
            GraphqlRequest(
                GetShopInfoGqlQuery(),
                ShopInfoResponse::class.java,
                params.parameters
            )
        val graphqlResponse: GraphqlResponse = graphqlRepository.response(listOf(graphqlRequest))
        val errors: List<GraphqlError>? =
            graphqlResponse.getError(ShopInfoResponse::class.java)
        return if (errors.isNullOrEmpty()) {
            val data =
                graphqlResponse.getData<ShopInfoResponse>(ShopInfoResponse::class.java)
            val shopInfo = data.shopInfoById.result.firstOrNull()
            if (shopInfo != null) {
                shopInfo
            } else {
                throw NullPointerException()
            }
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopID"

        fun createRequestParams(shopId: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putInt(PARAM_SHOP_ID, shopId)
            return requestParams
        }

        const val QUERY = """
            query shopInfoByID (${'$'}shopID: Int!){
                shopInfoByID(input:{shopIDs: [${'$'}shopID], fields:["status","other-shiploc"]}) {
                    result {
                        shippingLoc {
                            provinceID
                        } 
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
    }
}
