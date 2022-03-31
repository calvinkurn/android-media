package com.tokopedia.shopdiscount.manage.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shopdiscount.manage.data.request.GetSlashPriceProductListRequest
import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListResponse
import com.tokopedia.shopdiscount.utils.constant.CAMPAIGN
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetSlashPriceProductListUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : UseCase<GetSlashPriceProductListResponse>(), GqlQueryInterface {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return """
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
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return EMPTY_STRING
    }

    private var params = emptyMap<String, Any>()

    suspend fun execute(
        source: String = CAMPAIGN,
        ip: String = "",
        useCase: String = "",
        page: Int,
        pageSize: Int = 10,
        keyword: String = "",
        categoryIds: List<String> = emptyList(),
        etalaseIds: List<String> = emptyList(),
        warehouseIds: List<String> = emptyList()
    ): GetSlashPriceProductListResponse {
        val requestHeader = GetSlashPriceProductListRequest.RequestHeader(source, ip, useCase)
        val filter = GetSlashPriceProductListRequest.Filter(
            page,
            pageSize,
            keyword,
            categoryIds,
            etalaseIds,
            warehouseIds
        )
        val request = GetSlashPriceProductListRequest(requestHeader, filter)
        params = mapOf(REQUEST_PARAM_KEY to request)
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): GetSlashPriceProductListResponse {
        val request =
            GraphqlRequest(this, GetSlashPriceProductListResponse::class.java, params, true)
        return repository.response(listOf(request)).getSuccessData()
    }


}