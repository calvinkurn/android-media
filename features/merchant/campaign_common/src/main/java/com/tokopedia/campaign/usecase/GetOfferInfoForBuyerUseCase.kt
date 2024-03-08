package com.tokopedia.campaign.usecase

import com.tokopedia.campaign.data.request.GetOfferingInfoForBuyerRequestParam
import com.tokopedia.campaign.data.response.OfferInfoForBuyerResponse
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.ResponseErrorException
import javax.inject.Inject

class GetOfferInfoForBuyerUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<OfferInfoForBuyerResponse>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_INPUT = "input"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "getOfferingInfoForBuyer"
        private val QUERY = """
              query $OPERATION_NAME(${'$'}input: GetOfferingInfoForBuyerRequest!) {
                  GetOfferingInfoForBuyer(input: ${'$'}input) {
                     response_header{
                          status
                          errorMessage
                          errorCode
                          success
                          processTime
                        }
                        offering_json_data
                        nearest_warehouse_id
                        offering{
                          offer_id
                          offer_type_id
                          shop_data{
                            shop_id
                            shop_name
                            badge
                          }
                          offer_name
                          start_date
                          end_date
                          tier_list{
                            tier_id
                            level
                            tier_wording
                            rule{
                              type_id
                              operation
                              value
                            }
                            benefit{
                              type_id
                              value
                              product_info{
                                product_id
                                image_url
                                priority
                              }                         
                            }
                            is_oos
                          }
                          term_and_condition
                          offer_landing_page_link
                          offer_landing_page_app_link
                          upsell_wording
                        }
                      }
                    }


        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: GetOfferingInfoForBuyerRequestParam): OfferInfoForBuyerResponse {
        val request = buildRequest(param)
        val response = repository.response(listOf(request)).getSuccessData<OfferInfoForBuyerResponse>()
        val isError = !response.offeringInforBuyer.responseHeader.success
        if (isError) {
            throw ResponseErrorException(response.offeringInforBuyer.responseHeader.errorCode.toString())
        } else {
            return response
        }
    }

    private fun buildRequest(param: GetOfferingInfoForBuyerRequestParam): GraphqlRequest {
        val params = mapOf(
            REQUEST_PARAM_INPUT to param
        )

        return GraphqlRequest(
            query,
            OfferInfoForBuyerResponse::class.java,
            params
        )
    }
}
