package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.mapper.GetFlashSaleListForSellerMapper
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleListForSellerRequest
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleListForSellerResponse
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleData
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import javax.inject.Inject


class GetFlashSaleListForSellerUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetFlashSaleListForSellerMapper
) : GraphqlUseCase<FlashSaleData>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val DEFAULT_PAGE_SIZE = 10
    }

    private val query = object : GqlQueryInterface {

        private val OPERATION_NAME = "getFlashSaleListForSeller"
        private val QUERY = """
        query $OPERATION_NAME(${'$'}params: GetFlashSaleListForSellerRequest!) {
             $OPERATION_NAME(params: ${'$'}params) {
                total_campaign
                campaign_list {
                  campaign_id
                  name
                  slug
                  description
                  cover_image
                  submission_start_date_unix
                  submission_end_date_unix
                  review_start_date_unix
                  review_end_date_unix
                  start_date_unix
                  has_eligible_products
                  end_date_unix
                  remaining_quota
                  max_product_submission
                  status_id
                  status_text
                  cancellation_reason
                  use_multilocation
                  product_meta {
                    total_product
                    accepted_product
                    rejected_product
                    transferred_product
                    total_product_stock
                    total_stock_sold,
                    float_total_sold_value
                  }
                  product_criteria {
                    additional_info{
                      matched_product
                    }
                    criteria_id
                    min_price
                    max_price
                    min_final_price
                    max_final_price
                    min_discount
                    min_custom_stock
                    max_custom_stock
                    min_rating
                    min_product_score
                    min_qty_sold
                    max_qty_sold
                    max_submission
                    max_product_appear
                    day_periode_time_appear
                    categories {
                      category_id
                      category_name
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

    suspend fun execute(param: Param): FlashSaleData {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetFlashSaleListForSellerResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val requestHeader =
            CampaignParticipationRequestHeader(usecase = "campaign_list_${param.tabName}")
        val payload = GetFlashSaleListForSellerRequest(
            requestHeader,
            param.tabName,
            GetFlashSaleListForSellerRequest.Pagination(param.rows, param.offset),
            GetFlashSaleListForSellerRequest.Filter(
                param.campaignIds,
                param.categoryIds,
                param.statusIds
            ),
            GetFlashSaleListForSellerRequest.Sort(param.sortOrderBy, param.sortOrderRule),
            GetFlashSaleListForSellerRequest.AdditionalParam(
                productMeta = param.requestProductMetaData,
                checkProductEligibility = param.checkProductEligibility
            )
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            query,
            GetFlashSaleListForSellerResponse::class.java,
            params
        )
    }

    data class Param(
        val tabName: String,
        val offset: Int,
        val rows: Int = DEFAULT_PAGE_SIZE,
        val keyword: String = "",
        val campaignIds: List<Long> = emptyList(),
        val categoryIds: List<Long> = emptyList(),
        val statusIds: List<String> = listOf(FlashSaleStatus.DEFAULT.id),
        val sortOrderBy: String = "DEFAULT_VALUE_PLACEHOLDER",
        val sortOrderRule: String = "ASC",
        val requestProductMetaData: Boolean = false,
        val checkProductEligibility: Boolean = true
    )

}
