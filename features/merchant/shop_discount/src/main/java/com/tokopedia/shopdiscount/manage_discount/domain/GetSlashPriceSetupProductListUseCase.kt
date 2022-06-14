package com.tokopedia.shopdiscount.manage_discount.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.manage_discount.data.request.GetSlashPriceSetupProductListRequest
import com.tokopedia.shopdiscount.manage_discount.data.response.GetSlashPriceSetupProductListResponse
import javax.inject.Inject

class GetSlashPriceSetupProductListUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<GetSlashPriceSetupProductListResponse>(gqlRepository) {

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setGraphqlQuery(GetSlashPriceSetupProductListQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetSlashPriceSetupProductListResponse::class.java)
    }

    fun setParams(request: GetSlashPriceSetupProductListRequest) {
        setRequestParams(
            mapOf<String, Any>(
                KEY_PARAMS to request
            )
        )
    }

    companion object {
        private const val KEY_PARAMS = "params"
        private const val QUERY_NAME = "GetSlashPriceSetupProductListQuery"
        private const val QUERY = """
            query GetSlashPriceSetupProductListQuery(${'$'}params: GetSlashPriceSetupProductListRequest!) {
              getSlashPriceSetupProductList(params: ${'$'}params) {
                product_list {
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
                  is_expand
                  warehouses {
                    warehouse_id
                    warehouse_name
                    warehouse_location
                    warehouse_stock
                    max_order
                    event_id
                    abusive_rule
                    avg_sold_price
                    cheapest_price
                    discounted_price
                    discounted_percentage
                    min_recommendation_price
                    min_recommendation_percentage
                    max_recommendation_price
                    max_recommendation_percentage
                    disable
                    disable_recommendation
                    warehouse_type
                    original_price
                  }
                  slash_price_info {
                    slash_price_product_id
                    discounted_price
                    discount_percentage
                    start_date
                    end_date
                    slash_price_status_id
                  }
                  variants {
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
                    is_expand
                    warehouses {
                      warehouse_id
                      warehouse_name
                      warehouse_location
                      warehouse_stock
                      max_order
                      event_id
                      abusive_rule
                      avg_sold_price
                      cheapest_price
                      discounted_price
                      discounted_percentage
                      min_recommendation_price
                      min_recommendation_percentage
                      max_recommendation_price
                      max_recommendation_percentage
                      disable
                      disable_recommendation
                      warehouse_type
                      original_price
                    }
                    slash_price_info {
                      slash_price_product_id
                      discounted_price
                      discount_percentage
                      start_date
                      end_date
                      slash_price_status_id
                    }
                  }
                }
                response_header {
                  status
                  success
                  process_time
                  reason
                  error_code
                  error_message
                }
              }
            }
        """
    }
}