package com.tokopedia.shopdiscount.subsidy.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.product_detail.data.request.GetSlashPriceProductDetailRequest
import com.tokopedia.shopdiscount.product_detail.data.response.GetSlashPriceProductDetailResponse
import com.tokopedia.shopdiscount.subsidy.model.response.OptOutReasonListResponse
import javax.inject.Inject

class GetOptOutReasonListUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<OptOutReasonListResponse>(gqlRepository) {

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
//        setGraphqlQuery(GetOptOutReasonListQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(OptOutReasonListResponse::class.java)
    }

    companion object {
//        private const val KEY_PARAMS = "params"
        private const val QUERY_NAME = "GetOptOutReasonListQuery"
        private const val QUERY = """
//            query GetSlashPriceProductDetailQuery(${'$'}params: GetSlashPriceProductDetailRequest!) {
//              GetSlashPriceProductDetail(params: ${'$'}params) {
//                response_header {
//                  status
//                  success
//                  process_time
//                  reason
//                  error_code
//                  error_message
//                }
//                product_list {
//                  slash_price_product_id
//                  product_id
//                  name
//                  price {
//                    min
//                    min_formatted
//                    max
//                    max_formatted
//                  }
//                  stock
//                  url
//                  sku
//                  picture
//                  discounted_price
//                  discounted_percentage
//                  max_order
//                  start_date
//                  end_date
//                  warehouses {
//                    warehouse_id
//                    warehouse_name
//                    warehouse_location
//                    warehouse_stock
//                    max_order
//                    event_id
//                    discounted_price
//                    discounted_percentage
//                    original_price
//                  }
//                  is_variant
//                  is_expand
//                  parent_id
//                  subsidy_info {
//                    cta_program_link
//                    subsidy_type
//                    discounted_price
//                    discounted_percentage
//                    remaining_quota
//                    quota_subsidy
//                    subsidy_date_start
//                    subsidy_date_stop
//                    max_order
//                    seller_discount_price
//                    seller_discount_percentage
//                  }
//                  join_subsidy
//                  subsidy_status_text
//                }
//              }
//            }
//        """
    }
}
