package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.vouchercreation.product.create.data.response.GetProductsByProductIdResponse
import javax.inject.Inject


class GetProductsUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
) : UseCase<GetProductsByProductIdResponse.GetProductListData>() {

    private val query = """
            query GetShopProductList(${'$'}shopId: String!, ${'$'}filter: [GoodsFilterInput], ${'$'}sort: GoodsSortInput) {
                ProductList(shopID: ${'$'}shopId, filter: ${'$'}filter, sort: ${'$'}sort) {
                    header {
                        messages
                        reason
                        errorCode
                    }
                    data {
                        id
                        name
                        pictures {
                            urlThumbnail
                        }
                        txStats {
                            sold
                        }
                    }
                    meta {
                        totalHits
                    }
                }  
            }
        """

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): GetProductsByProductIdResponse.GetProductListData {
        val graphqlRequest =
            GraphqlRequest(query, GetProductsByProductIdResponse::class.java, params)
        val graphqlResponse: GraphqlResponse = graphqlRepository.response(listOf(graphqlRequest))
        val response =
            graphqlResponse.getData<GetProductsByProductIdResponse>(GetProductsByProductIdResponse::class.java)
        val errors = response?.productList?.header?.messages.orEmpty()
        if (response != null && errors.isNullOrEmpty()) {
            return response.productList
        } else {
            throw MessageErrorException(errors.joinToString(","))
        }

    }

    companion object {

        private const val PARAMS_SHOP_ID = "shopId"
        private const val PARAMS_FILTER = "filter"
        private const val PARAMS_SORT = "sort"
        private const val PARAMS_ID = "id"
        private const val PARAMS_VALUE = "value"

        private const val PARAMS_INPUT_PRODUCT_ID = "productIDInclude"

        private const val PARAMS_INPUT_SOLD = "SOLD"

        fun createParams(
            shopId: String,
            productIds: List<Long>
        ): Map<String, Any> = mapOf(
            PARAMS_SHOP_ID to shopId,
            PARAMS_FILTER to mutableListOf(
                mapOf(
                    PARAMS_ID to PARAMS_INPUT_PRODUCT_ID,
                    PARAMS_VALUE to populateProductId(productIds)
                )
            ),
            PARAMS_SORT to mapOf(PARAMS_INPUT_SOLD to "DESC")
        )

        private fun populateProductId(productIds: List<Long>): List<String> {
            return productIds.map { it.toString() }
        }
    }
}