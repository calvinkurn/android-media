package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.mapper.GetFlashSaleProductCriteriaCheckingMapper
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleProductCriteriaCheckingRequest
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleProductCriteriaCheckingResponse
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import javax.inject.Inject

class GetFlashSaleProductCriteriaCheckingUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetFlashSaleProductCriteriaCheckingMapper
) : GraphqlUseCase<List<CriteriaCheckingResult>>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "getFlashSaleProductCriteriaChecking"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}params: GetFlashSaleProductCriteriaCheckingRequest!) {
              $OPERATION_NAME(params:${'$'}params) {
                response_header {
                  status
                  success
                  process_time
                  error_code
                }
                product_list {
                  name
                  picture_url
                  is_multiwarehouse
                  category {
                    is_eligible
                    name
                  }
                  rating {
                    is_eligible
                    min_rating
                  }
                  product_score {
                    is_eligible
                    min_product_score
                  }
                  count_sold {
                    is_eligible
                    min_count_sold
                    max_count_sold
                  }
                  min_order {
                    is_eligible
                    min_order
                  }
                  max_appearance {
                    is_eligible
                    max_appearance
                  }
                  exclude_wholesale {
                    is_active
                    is_eligible
                  }
                  exclude_second_hand {
                    is_active
                    is_eligible
                  }
                  exclude_pre_order {
                    is_active
                    is_eligible
                  }
                  free_ongkir {
                    is_active
                    is_eligible
                  }
                  warehouses {
                    warehouse_id
                    city_name
                    is_dilayani_tokopedia
                    price {
                      is_eligible
                      min_price
                      max_price
                    }
                    stock {
                      is_eligible
                      min_stock
                      cta_link
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

    suspend fun execute(
        campaignId: Long,
        productId: Long,
        productCriteriaId: Long
    ): List<CriteriaCheckingResult> {
        val request = buildRequest(campaignId, productId, productCriteriaId)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetFlashSaleProductCriteriaCheckingResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(
        campaignId: Long,
        productId: Long,
        productCriteriaId: Long
    ): GraphqlRequest {
        val requestHeader = CampaignParticipationRequestHeader(usecase = "product_nomination")
        val payload = GetFlashSaleProductCriteriaCheckingRequest(
            requestHeader,
            campaignId,
            productId,
            productCriteriaId
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            query,
            GetFlashSaleProductCriteriaCheckingResponse::class.java,
            params
        )
    }

}
