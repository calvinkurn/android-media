package com.tokopedia.mvc.domain.usecase


import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.mapper.ProductListMapper
import com.tokopedia.mvc.data.request.GoodsFilterInput
import com.tokopedia.mvc.data.request.GoodsSortInput
import com.tokopedia.mvc.data.response.ProductListResponse
import com.tokopedia.mvc.domain.entity.ProductResult
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductListUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: ProductListMapper,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<ProductResult>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_SHOP_ID = "shopID"
        private const val REQUEST_PARAM_FILTER = "filter"
        private const val REQUEST_PARAM_SORT = "sort"
        private const val REQUEST_PARAM_EXTRA_INFO = "extraInfo"
        private const val REQUEST_PARAM_WAREHOUSE_ID = "warehouseID"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "ProductList"
        private val QUERY = """
           query $OPERATION_NAME(${'$'}shopID: String!, ${'$'}filter: [GoodsFilterInput], ${'$'}sort: GoodsSortInput, ${'$'}extraInfo: [String], ${'$'}warehouseID: String) {
              $OPERATION_NAME(shopID: ${'$'}shopID, filter: ${'$'}filter, sort: ${'$'}sort, extraInfo: ${'$'}extraInfo, warehouseID: ${'$'}warehouseID) {
                meta {
                  totalHits
                }
                data {
                  id
                  name
                  price {
                    min
                    max
                  }
                  stock
                  status
                  isVariant
                  sku
                  warehouseCount
                  preorder {
                    durationDays
                  }
                  warehouse {
                    id
                  }
                  stats {
                    countView
                    countReview
                    countTalk
                  }
                  txStats {
                    sold
                  }
                  pictures {
                    urlThumbnail
                  }
                }
              }
           }

    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): ProductResult {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<ProductListResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val shopId = userSession.shopId
        val filter = mutableListOf(
            GoodsFilterInput("page", listOf(param.page.toString())),
            GoodsFilterInput("pageSize", listOf(param.pageSize.toString())),
            GoodsFilterInput("status", listOf("ACTIVE"))
        )

        if (param.categoryIds.isNotEmpty()) {
            filter.add(GoodsFilterInput("category", param.categoryIds.map { it.toString() }))
        }

        if (param.searchKeyword.isNotEmpty()) {
            filter.add(GoodsFilterInput("keyword", listOf(param.searchKeyword)))
        }

        if (param.showcaseIds.isNotEmpty()) {
            filter.add(GoodsFilterInput("menu", param.showcaseIds.map { it.toString() }))
        }

        if (param.productIdInclude.isNotEmpty()) {
            filter.add(GoodsFilterInput("productIDInclude", param.productIdInclude.map { it.toString() }))
        }

        val sort = GoodsSortInput(param.sortId, param.sortDirection)
        val params = mapOf(
            REQUEST_PARAM_SHOP_ID to shopId,
            REQUEST_PARAM_FILTER to filter,
            REQUEST_PARAM_SORT to sort,
            REQUEST_PARAM_EXTRA_INFO to param.extraInfo,
            REQUEST_PARAM_WAREHOUSE_ID to param.warehouseId.toString()
        )

        return GraphqlRequest(
            query,
            ProductListResponse::class.java,
            params
        )
    }


    data class Param(
        val searchKeyword: String,
        val warehouseId: Long,
        val page: Int,
        val pageSize: Int,
        val categoryIds: List<Long>,
        val showcaseIds : List<Long>,
        val sortId : String,
        val sortDirection:String,
        val extraInfo: List<String> = listOf("view"),
        val productIdInclude : List<Long> = emptyList()
    )


}

