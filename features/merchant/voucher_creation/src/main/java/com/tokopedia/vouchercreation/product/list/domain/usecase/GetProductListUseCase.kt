package com.tokopedia.vouchercreation.product.list.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.product.list.domain.model.request.GoodsFilterInput
import com.tokopedia.vouchercreation.product.list.domain.model.request.GoodsSortInput
import com.tokopedia.vouchercreation.product.list.domain.model.response.ProductListResponse
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(@ApplicationContext repository: GraphqlRepository)
    : GraphqlUseCase<ProductListResponse>(repository) {

    companion object {

        private const val KEY_KEYWORD = "keyword"
        private const val KEY_MENU = "menu"
        private const val KEY_CATEGORY = "category"
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_WAREHOUSE_ID = "warehouseID"
        private const val KEY_FILTER = "filter"
        private const val KEY_SORT = "sort"
        private const val KEY_PAGE = "page"
        private const val KEY_PAGE_SIZE = "pageSize"
        private const val PAGE_SIZE = "10"

        @JvmStatic
        fun createRequestParams(
                page: Int? = null,
                keyword: String? = null,
                shopId: String?,
                warehouseId: String? = null,
                shopShowCaseIds: List<String>? = null,
                categories: List<String>? = null,
                sort: GoodsSortInput? = null): RequestParams {

            val filtersParams = mutableListOf<GoodsFilterInput>().apply {
                keyword?.run { add(GoodsFilterInput(id = KEY_KEYWORD, value = listOf(this))) }
                shopShowCaseIds?.run { add(GoodsFilterInput(id = KEY_MENU, value = shopShowCaseIds)) }
                categories?.run { add(GoodsFilterInput(id = KEY_CATEGORY, value = categories)) }
                page?.run { add(GoodsFilterInput(id = KEY_PAGE, value = listOf(this.toString()))) }
                add(GoodsFilterInput(id = KEY_PAGE_SIZE, value = listOf(PAGE_SIZE)))
            }

            return RequestParams.create().apply {
                putString(KEY_SHOP_ID, shopId)
                putString(KEY_WAREHOUSE_ID, warehouseId)
                putObject(KEY_FILTER, filtersParams)
                putObject(KEY_SORT, sort)
            }
        }
    }

    private val query = """      
        query ProductList(${'$'}shopID: String!, ${'$'}warehouseID: String, ${'$'}filter: [GoodsFilterInput], ${'$'}sort:GoodsSortInput) {
            ProductList(shopID: ${'$'}shopID, warehouseID: ${'$'}warehouseID, filter: ${'$'}filter, sort: ${'$'}sort) {
                data {
                  id
                  name
                  price {
                    min
                    max
                  }
                  stock
                  stockReserved
                  status
                  minOrder
                  maxOrder
                  weight
                  weightUnit
                  condition
                  isMustInsurance
                  isKreasiLokal
                  isCOD
                  isVariant
                  isEmptyStock
                  hasStockReserved
                  hasInbound
                  url
                  sku
                  cashback
                  featured
                  score {
                    total
                  }
                  category {
                    id
                  }
                  menu {
                    id
                  }
                  pictures {
                    urlThumbnail
                  }
                  position {
                    position
                  }
                  shop {
                    id
                  }
                  wholesale {
                    minQty
                    price
                  }
                  stats {
                    countView
                    countReview
                    countTalk
                  }
                  txStats {
                    sold
                  }
                  lock {
                    full
                    partial {
                      price
                      stock
                      status
                      wholesale
                    }
                  }
                  tax {
                    warehouseID
                    originalPrice
                    importTax
                    incomeTax
                    serviceTax
                  }
                  topads {
                    status
                    management
                  }
                  warehouseCount
                  priceSuggestion {
                    suggestedPrice
                    suggestedPriceTreshold
                    suggestedPriceMin
                    suggestedPriceMax
                    label
                    updateTime
                  }
                  campaignType {
                    id
                    name
                    iconURL
                  }
                  suspendLevel
                }
            }
        }
    """.trimIndent()

    init {
        setGraphqlQuery(query)
        setTypeClass(ProductListResponse::class.java)
    }
}