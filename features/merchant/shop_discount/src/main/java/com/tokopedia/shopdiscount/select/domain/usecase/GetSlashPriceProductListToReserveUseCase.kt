package com.tokopedia.shopdiscount.select.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.select.data.request.FilterRequest
import com.tokopedia.shopdiscount.select.data.request.GetSlashPriceProductListToReserveRequest
import com.tokopedia.shopdiscount.select.data.request.SortRequest
import com.tokopedia.shopdiscount.select.data.response.GetSlashPriceProductListToReserveResponse
import com.tokopedia.shopdiscount.utils.constant.CAMPAIGN
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetSlashPriceProductListToReserveUseCase @Inject constructor(
    private val repository: GraphqlRepository,
) : GraphqlUseCase<GetSlashPriceProductListToReserveResponse>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val REQUEST_QUERY_NAME = "getSlashPriceProductListToReserve"
        private const val REQUEST_QUERY = """
            query getSlashPriceProductListToReserve(${'$'}params: GetSlashPriceProductListToReserveRequest!)  {
              getSlashPriceProductListToReserve(params: ${'$'}params){
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
                  count_variant
                  disabled
                  disabled_reason
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

    init {
        setupUseCase()
    }

    @GqlQuery(
        REQUEST_QUERY_NAME,
        REQUEST_QUERY
    )

    private fun setupUseCase() {
        setGraphqlQuery(GetSlashPriceProductListToReserve())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetSlashPriceProductListToReserveResponse::class.java)
    }

    fun setParams(
        source: String = CAMPAIGN,
        ip: String = EMPTY_STRING,
        useCase: String = EMPTY_STRING,
        sortBy: String= EMPTY_STRING,
        sortType: String = EMPTY_STRING,
        requestId : String,
        page: Int,
        pageSize: Int = 10,
        keyword: String = EMPTY_STRING,
        showcaseIds: List<String>  = emptyList(),
        categoryIds: List<String> = emptyList(),
        warehouseIds: List<String> = emptyList()
    ) {

        val request = GetSlashPriceProductListToReserveRequest(
            RequestHeader(source, ip, useCase),
            requestId,
            FilterRequest(page, pageSize, keyword, showcaseIds, categoryIds, warehouseIds),
            SortRequest(sortBy, sortType)
        )
        val params = mapOf(REQUEST_PARAM_KEY to request)
        setRequestParams(params)
    }


}