package com.tokopedia.campaign.usecase

import com.tokopedia.campaign.data.request.GetOfferingProductListRequestParam
import com.tokopedia.campaign.data.response.OfferProductListResponse
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

class GetOfferProductListUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<OfferProductListResponse>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAMS = "input"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "getOfferingProductList"
        private val QUERY = """
              query $OPERATION_NAME(${'$'}input: GetOfferingProductListRequest!) {
                  GetOfferingProductList(input: ${'$'}input) {
                     response_header {
                          status
                          errorMessage
                          errorCode
                          success
                          processTime
                        }
                        products {
                          offer_id
                          parent_id
                          product_id
                          warehouse_id
                          product_url
                          image_url
                          name
                          price
                          rating
                          sold_count
                          stock
                          is_vbs
                          min_order
                          campaign {
                            name
                            original_price
                            discounted_price
                            discounted_percentage
                            custom_stock
                          }
                          label_group {
                            position
                            title
                            type
                            url
                          }
                        }
                        total_product
                        pagination {
                          current_page
                          next_page
                          prev_page
                          has_next
                        }
                      }
                    }



        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: GetOfferingProductListRequestParam): OfferProductListResponse {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        return response.getSuccessData()
    }

    private fun buildRequest(param: GetOfferingProductListRequestParam): GraphqlRequest {
        val params = mapOf(
            REQUEST_PARAMS to param
        )

        return GraphqlRequest(
            query,
            OfferProductListResponse::class.java,
            params
        )
    }
}
