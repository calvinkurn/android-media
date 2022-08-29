package com.tokopedia.shopdiscount.manage.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.manage.data.request.GetSlashPriceProductListRequest
import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListResponse
import com.tokopedia.shopdiscount.utils.constant.CAMPAIGN
import javax.inject.Inject

class GetSlashPriceProductListUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<GetSlashPriceProductListResponse>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val REQUEST_QUERY_NAME = "GetSlashPriceProductList"
        private const val REQUEST_QUERY = """
            query GetSlashPriceProductList(${'$'}params: GetSlashPriceProductListRequest!) {
              GetSlashPriceProductList(params: ${'$'}params) {
                response_header {
                  status
                  success
                }
                slash_price_product_list {
                  slash_price_product_id
                  product_id
                  name
                  price {
                    min
                    min_formatted
                    max
                    max_formatted
                  }
                  stock
                  url
                  sku
                  picture
                  discounted_price
                  discounted_percentage
                  max_order
                  start_date
                  end_date
                  warehouses {
                    warehouse_id
                    warehouse_name
                    warehouse_location
                    warehouse_stock
                    max_order
                    event_id
                    original_price
                    discounted_price
                    discounted_percentage
                  }
                  is_variant
                  is_expand
                  discounted_price_data {
                    min
                    min_formatted
                    max
                    max_formatted
                  }
                  discount_percentage_data {
                    min
                    min_formatted
                    max
                    max_formatted
                  }
                }
                total_product
              }
            }
        """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(REQUEST_QUERY_NAME, REQUEST_QUERY)
    private fun setupUseCase() {
        setGraphqlQuery(GetSlashPriceProductList())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetSlashPriceProductListResponse::class.java)
    }

    fun setRequestParams(
        source: String = CAMPAIGN,
        ip: String = "",
        useCase: String = "",
        page: Int,
        pageSize: Int = 10,
        keyword: String = "",
        categoryIds: List<String> = emptyList(),
        etalaseIds: List<String> = emptyList(),
        warehouseIds: List<String> = emptyList(),
        status: Int
    ) {
        val requestHeader = RequestHeader(source, ip, useCase)
        val filter = GetSlashPriceProductListRequest.Filter(
            page,
            pageSize,
            keyword,
            categoryIds,
            etalaseIds,
            warehouseIds,
            status
        )
        val request = GetSlashPriceProductListRequest(requestHeader, filter)
        val params = mapOf(REQUEST_PARAM_KEY to request)
        setRequestParams(params)
    }

}