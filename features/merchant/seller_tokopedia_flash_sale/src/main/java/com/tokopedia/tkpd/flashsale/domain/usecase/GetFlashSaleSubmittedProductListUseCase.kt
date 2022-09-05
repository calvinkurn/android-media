package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.mapper.GetFlashSaleSubmittedProductListMapper
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleSubmittedProductListRequest
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleSubmittedProductListRequest.*
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleSubmittedProductListResponse
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProductData
import javax.inject.Inject

class GetFlashSaleSubmittedProductListUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetFlashSaleSubmittedProductListMapper
) : GraphqlUseCase<SubmittedProductData>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY_NAME = "getFlashSaleSubmittedProductList"
        private const val QUERY = """
            query $QUERY_NAME(${'$'}params: GetFlashSaleSubmittedProductListRequest!) {
                $QUERY_NAME(params: ${'$'}params) {
                    response_header {
                        status
                        success
                        process_time
                        error_code
                    }
                    data {
                        product_list {
                            product_id
                            name
                            url
                            picture
                            main_stock
                            campaign_stock
                            is_multiwarehouse
                            is_parent_product
                            total_child
                            price {
                              price
                              lower_price
                              upper_price
                            }
                            discount {
                              discount
                              lower_discount
                              upper_discount
                            }
                            discounted_price {
                              price
                              lower_price
                              upper_price
                            }
                            warehouses {
                              warehouse_id
                              name
                              stock
                              price
                              status_id
                              status_text
                              rejection_reason
                            }
                            product_criteria {
                               criteria_id
                            }
                        }
                    total_product
                }
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
        productId: Long = 0,
        pagination: Pagination = Pagination(),
        filter: Filter = Filter()
    ): SubmittedProductData {
        val request = buildRequest(campaignId = campaignId, pagination = pagination, filter = filter)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetFlashSaleSubmittedProductListResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(
        campaignId: Long,
        productId: Long = 0,
        pagination: Pagination,
        filter : Filter
    ): GraphqlRequest {
        val payload = GetFlashSaleSubmittedProductListRequest(
            requestHeader = SubmittedProductListRequestHeader(),
            campaignId = campaignId.toInt(),
            productId = productId.toInt(),
            pagination = pagination,
            filter = filter,
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetFlashSaleSubmittedProductList(),
            GetFlashSaleSubmittedProductListResponse::class.java,
            params
        )
    }
}