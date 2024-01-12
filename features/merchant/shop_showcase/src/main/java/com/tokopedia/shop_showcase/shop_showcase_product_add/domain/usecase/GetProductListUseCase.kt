package com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop_showcase.shop_showcase_product_add.data.model.ProductListResponse
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.mapper.ProductMapper
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GoodsFilterInput
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GoodsFilterInput.Companion.FILTER_ID_CATEGORY
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GoodsFilterInput.Companion.FILTER_ID_KEYWORD
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GoodsFilterInput.Companion.FILTER_ID_MENU
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GoodsFilterInput.Companion.FILTER_ID_PAGE
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GoodsFilterInput.Companion.FILTER_ID_PAGE_SIZE
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GoodsSortInput
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * @author by Rafli Syam on 2020-03-09
 */

class GetProductListUseCase(
        private val gqlRepository: GraphqlRepository,
        private val productMapper: ProductMapper
) : UseCase<List<ShowcaseProduct>>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): List<ShowcaseProduct> {
        val request = GraphqlRequest(QUERY, ProductListResponse::class.java, params.parameters)
        val response = gqlRepository.response(listOf(request))
        val responseData = response.getData<ProductListResponse>(ProductListResponse::class.java)
        return productMapper.mapToUIModel(responseData.productList.data)
    }

    companion object {

        /**
         * Request Param Keys
         */
        private const val SHOP_ID = "shopID"
        private const val FILTER = "filter"
        private const val SORT = "sort"

        /**
         * Create request parameters for get product list gql call
         */
        fun createRequestParams(
                shopId: String,
                filter: GetProductListFilter
        ): RequestParams = RequestParams.create().apply {
            putString(SHOP_ID, shopId)
            putObject(FILTER, createFilterInput(filter))
            putObject(SORT, createSortInput(filter))
        }

        private fun createSortInput(filter: GetProductListFilter): GoodsSortInput {
            return GoodsSortInput(
                filter.sortId,
                filter.sortValue
            )
        }

        private fun createFilterInput(filter: GetProductListFilter): List<GoodsFilterInput> {
            return mutableListOf(
                GoodsFilterInput(FILTER_ID_PAGE, listOf(filter.page.toString())),
                GoodsFilterInput(FILTER_ID_PAGE_SIZE, listOf(filter.perPage.toString())),
                GoodsFilterInput(FILTER_ID_KEYWORD, listOf(filter.fkeyword)),
            ).apply {
                if (filter.fcategory != Int.ZERO) {
                    add(GoodsFilterInput(FILTER_ID_CATEGORY, listOf(filter.fcategory.toString())))
                }
                if (filter.fmenu?.isNotEmpty() == true) {
                    add(GoodsFilterInput(FILTER_ID_MENU, listOf(filter.fmenu.orEmpty())))
                }
            }
        }

        /**
         * GQL Query for get product list by shop ID
         */
        val QUERY = """
            query ProductList(
                ${'$'}shopID: String!,
                ${'$'}filter: [GoodsFilterInput],
                ${'$'}sort: GoodsSortInput
            ) {
              ProductList(shopID: ${'$'}shopID, filter: ${'$'}filter, sort: ${'$'}sort) {
                header {
                  processTime
                  messages
                  reason
                  errorCode
                }
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
                  stockReserved
                  status
                  minOrder
                  maxOrder
                  weight
                  weightUnit
                  condition
                  isMustInsurance
                  isCOD
                  isVariant
                  isEmptyStock
                  hasStockReserved
                  hasInbound
                  url
                  sku
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
                    productRecommendation {
                      productID
                      title
                      price
                      imageURL
                      sold
                      rating
                    }
                  }
                  campaignType {
                    id
                    name
                    iconURL
                  }
                  suspendLevel
                  hasAddon
                  addonID
                  hasStockAlert
                  stockAlertCount
                  stockAlertActive
                  isMultipleWarehouse
                  haveNotifyMeOOS
                  notifyMeOOSCount
                  notifyMeOOSWording
                  manageProductData {
                    isPriceUncompetitive
                    isStockGuaranteed
                    scoreV3
                    isTobacco
                    isDTInbound
                    isInGracePeriod
                    isArchived
                  }
                  createTime
                }
              }
            }
        """.trimIndent()
    }
}
