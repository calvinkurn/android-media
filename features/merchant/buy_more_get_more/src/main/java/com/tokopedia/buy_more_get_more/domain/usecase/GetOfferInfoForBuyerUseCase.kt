package com.tokopedia.buy_more_get_more.domain.usecase

import com.tokopedia.buy_more_get_more.data.mapper.GetOfferInfoForBuyerMapper
import com.tokopedia.buy_more_get_more.data.response.OfferInfoForBuyerResponse
import com.tokopedia.buy_more_get_more.domain.entity.OfferInfoForBuyer
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

class GetOfferInfoForBuyerUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetOfferInfoForBuyerMapper
) : GraphqlUseCase<OfferInfoForBuyer>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_OFFER_IDS = "offer_ids"
        private const val REQUEST_PARAM_SOURCE = "source"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "getOfferingInfoForBuyer"
        private val QUERY = """
              query $OPERATION_NAME(${'$'}offer_ids: [Int], ${'$'}source: String) {
                  $OPERATION_NAME(offer_ids: ${'$'}offer_ids, source: ${'$'}source) {
                     response_header{
                      status
                      error_message
                      success
                      process_time
                      reason
                      error_code
                    }
                    offering_json_data
                    offering{
                      offer_id
                      offer_name
                      shop_name
                      start_date
                      end_date
                      tier_list{
                        tier_id
                        level
                        rule{
                          type_id
                          operation
                          value
                        }
                        benefit{
                          type_id
                          value
                        }
                      }  
                    }
                  }
                }


        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): OfferInfoForBuyer {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<OfferInfoForBuyerResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val params = mapOf(
            REQUEST_PARAM_OFFER_IDS to param.offerIds,
            REQUEST_PARAM_SOURCE to param.source
        )

        return GraphqlRequest(
            query,
            OfferInfoForBuyerResponse::class.java,
            params
        )
    }

    data class Param(
        val offerIds: List<Int> = emptyList(),
        val shopId: Int = 0,
        val source: String = "shop"
    )
}
