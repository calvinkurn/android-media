package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flashsale.data.mapper.SellerCampaignProductListMapper
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignProductListResponse
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import javax.inject.Inject

class GetSellerCampaignProductListUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: SellerCampaignProductListMapper
) : GraphqlUseCase<SellerCampaignProductList>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val ZERO = 0
        private const val QUERY_NAME = "GetSellerCampaignProductList"
        private const val QUERY = """
            query GetSellerCampaignProductList(${'$'}params: GetSellerCampaignProductListRequest!) {
                getSellerCampaignProductList(params: ${'$'}params) {
                    response_header {
                        status
                        success
                        processTime
                    },
                    product_list {
                        product_id
                        parent_id
                        product_name
                        product_url
                        product_sku
                        price
                        formatted_price
                        stock
                        view_count
                        highlight_product_wording
                        image_url{
                            img_100square,
                            img_200,
                            img_300,
                            img_700
                        },
                        product_map_data {
                            product_map_id
                            campaign_id
                            product_map_status
                            product_map_admin_status
                            original_price
                            discounted_price
                            discount_percentage
                            custom_stock
                            original_custom_stock
                            original_stock
                            campaign_sold_count
                            max_order
                        },
                        warehouse_list {
                            warehouse_id
                            warehouse_name
                            stock
                            chosen_warehouse
                        }
                    },
                    total_product,
                    total_product_sold,
                    count_accepted_product,
                    total_product_qty,
                    total_income,
                    total_income_formatted,
                    product_failed_count
                }
            }
        """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    suspend fun execute(
        campaignId: Long,
        productName: String = "",
        listType: Int,
        pagination: GetSellerCampaignProductListRequest.Pagination
    ): SellerCampaignProductList {
        val request = buildRequest(campaignId, productName, listType, pagination)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetSellerCampaignProductListResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(
        campaignId: Long,
        productName: String,
        listType: Int,
        pagination: GetSellerCampaignProductListRequest.Pagination
    ): GraphqlRequest {
        val payload = GetSellerCampaignProductListRequest(
            campaignType = ZERO,
            listType = listType,
            pagination = pagination,
            filter = GetSellerCampaignProductListRequest.Filter(
                campaignId = campaignId,
                productName = productName
            )
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetSellerCampaignProductList(),
            GetSellerCampaignProductListResponse::class.java,
            params
        )
    }
}