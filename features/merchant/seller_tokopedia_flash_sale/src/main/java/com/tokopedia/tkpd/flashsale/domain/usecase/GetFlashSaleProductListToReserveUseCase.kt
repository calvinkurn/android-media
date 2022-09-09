package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.mapper.GetFlashSaleProductListToReserveMapper
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleProductListToReserveRequest
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleProductListToReserveResponse
import com.tokopedia.tkpd.flashsale.domain.entity.ProductToReserve
import javax.inject.Inject

class GetFlashSaleProductListToReserveUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetFlashSaleProductListToReserveMapper
) : GraphqlUseCase<ProductToReserve>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "getFlashSaleProductListToReserve"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}params: GetFlashSaleProductListToReserveRequest!) {
              $OPERATION_NAME(params: ${'$'}params) {
                submitted_product_ids
                total_product
                product_list {
                  product_id
                  name
                  picture_url
                  price {
                    price
                    lower_price
                    upper_price
                  }
                  stock
                  count_view
                  count_sold
                  count_eligible_warehouses
                  product_criteria {
                    criteria_id
                  }
                  disable_detail {
                    is_disabled
                    disable_title
                    disable_description
                    show_criteria_checking_cta
                  }
                  variant_meta {
                    count_variants
                    count_eligible_variants
                  }
                }
              }
            }
        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): ProductToReserve {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetFlashSaleProductListToReserveResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val requestHeader = CampaignParticipationRequestHeader(usecase = "list")
        val payload = GetFlashSaleProductListToReserveRequest(
            requestHeader = requestHeader,
            campaignId = param.campaignId,
            listType = param.listType,
            pagination = GetFlashSaleProductListToReserveRequest.Pagination(
                rows = param.row,
                offset = param.offset,
            ),
            filter = GetFlashSaleProductListToReserveRequest.Filter(
                categoryIds = param.filterCategoryIds,
                cityIds = param.filterCityIds,
                keyword = param.filterKeyword
            )
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            query,
            GetFlashSaleProductListToReserveResponse::class.java,
            params
        )
    }

    data class Param(
        val campaignId: Long = 0L,
        val listType: String = "",
        val filterCategoryIds: List<Long> = emptyList(),
        val filterCityIds: List<Long> = emptyList(),
        val filterKeyword: String = "",
        val row: Int = 0,
        val offset: Int = 0,
    )

}