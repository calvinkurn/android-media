package com.tokopedia.product.addedit.preview.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.preview.data.model.responses.ShopInfoLocationResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

import javax.inject.Inject

class GetShopInfoLocationUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): UseCase<Boolean>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): Boolean {
        val graphqlRequest = GraphqlRequest(getQuery(), ShopInfoLocationResponse::class.java, params.parameters)
        val graphqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest))
        val errors: List<GraphqlError>? = graphqlResponse.getError(ShopInfoLocationResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = graphqlResponse.getData<ShopInfoLocationResponse>(ShopInfoLocationResponse::class.java)
            return data.shopInfoById.result.firstOrNull()?.shippingLoc?.provinceId != 0
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

        private fun getQuery() = """
            query shopInfoByID (${'$'}shopID: Int!){
                shopInfoByID(input:{shopIDs: [${'$'}shopID], fields:["other-shiploc"]}) {
                    result {
                        shippingLoc {
                            provinceID
                        }                        
                    }
                }
            }    
            """.trimIndent()
    }
}