package com.tokopedia.vouchercreation.product.list.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.product.list.domain.model.request.GoodsFilterInput
import com.tokopedia.vouchercreation.product.list.domain.model.request.GoodsSortExtraInput
import com.tokopedia.vouchercreation.product.list.domain.model.request.GoodsSortInput
import com.tokopedia.vouchercreation.product.list.domain.model.response.ProductListResponse
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(@ApplicationContext repository: GraphqlRepository)
    : GraphqlUseCase<ProductListResponse>(repository) {

    companion object {

        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_WAREHOUSE_ID = "warehouseID"
        private const val KEY_FILTER = "filter"
        private const val KEY_SORT = "sort"

        @JvmStatic
        fun createRequestParams(shopId: String,
                         warehouseId: String? = null,
                         filter: GoodsFilterInput? = null,
                         sort: GoodsSortInput? = null,
                         sortExtra: List<GoodsSortExtraInput>? = null): RequestParams {
            return RequestParams.create().apply {
                putString(KEY_SHOP_ID, shopId)
                putString(KEY_WAREHOUSE_ID, warehouseId)
                putObject(KEY_FILTER, filter)
                putObject(KEY_SORT, sort)
            }
        }
    }

    private val query = """      
        query ProductList(${'$'}shopID: String!, ${'$'}warehouseID: String, ${'$'}filter: GoodsFilterInput, ${'$'}sort:GoodsSortInput) {
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