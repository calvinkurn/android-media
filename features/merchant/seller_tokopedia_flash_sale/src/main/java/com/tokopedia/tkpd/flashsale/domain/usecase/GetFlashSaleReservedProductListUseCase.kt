package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.mapper.GetFlashSaleReservedProductListMapper
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleReservedProductListRequest
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleReservedProductListResponse
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import javax.inject.Inject


class GetFlashSaleReservedProductListUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetFlashSaleReservedProductListMapper
) : GraphqlUseCase<ReservedProduct>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val DEFAULT_PAGE_SIZE = 10
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "getFlashSaleReservedProductList"
        private val QUERY = """
        query $OPERATION_NAME(${'$'}params: GetFlashSaleReservedProductListRequest!) {
             $OPERATION_NAME(params: ${'$'}params) {
                response_header {
                  status
                  error_message
                  success
                  error_code
                }
                total_product
                product_list {
                  product_id
                  name
                  sku
                  url
                  picture
                  stock
                  price {
                    price
                    lower_price
                    upper_price
                  }
                  is_multiwarehouse
                  is_parent_product
                  product_criteria {
                    criteria_id
                    min_custom_stock
                    max_custom_stock
                    min_final_price
                    max_final_price
                    min_discount
                    max_discount
                  }
                  warehouses {
                    warehouse_id
                    name
                    stock
                    price
                    discount_setup {
                      price
                      stock
                      discount
                    }
                    is_dilayani_tokopedia
                    is_toggle_on
                    is_disabled
                    disabled_reason
                  }
                  child_products {
                    product_id
                    name
                    sku
                    url
                    picture
                    stock
                    price {
                      price
                      lower_price
                      upper_price
                    }
                    is_multiwarehouse
                    is_toggle_on
                    warehouses {
                      warehouse_id
                      name
                      stock
                      price
                      discount_setup {
                        price
                        stock
                        discount
                      }
                      is_dilayani_tokopedia
                      is_toggle_on
                      is_disabled
                      disabled_reason
                    }
                    is_disabled
                    disabled_reason
                  }
                }
             }
       }

    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): ReservedProduct {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetFlashSaleReservedProductListResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val requestHeader = CampaignParticipationRequestHeader(usecase = "campaign_detail")
        val pagination = GetFlashSaleReservedProductListRequest.Pagination(param.rows, param.offset)
        val filter = GetFlashSaleReservedProductListRequest.Filter(param.keyword, param.categoryIds)
        val payload = GetFlashSaleReservedProductListRequest(requestHeader, param.reservationId, pagination, filter)

        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            query,
            GetFlashSaleReservedProductListResponse::class.java,
            params
        )
    }

    data class Param(
        val reservationId: String,
        val offset : Int,
        val rows: Int = DEFAULT_PAGE_SIZE,
        val keyword : String = "",
        val categoryIds: List<Long> = emptyList()
    )

}
