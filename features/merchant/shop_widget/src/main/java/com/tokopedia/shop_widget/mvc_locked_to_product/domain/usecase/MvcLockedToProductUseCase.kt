package com.tokopedia.shop_widget.mvc_locked_to_product.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductRequest
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductResponse
import javax.inject.Inject

class MvcLockedToProductUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<MvcLockedToProductResponse>(gqlRepository) {

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setGraphqlQuery(ShopPageMvcLockToProductQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(MvcLockedToProductResponse::class.java)
    }

    fun setParams(request: MvcLockedToProductRequest) {
        val districtId = request.districtID.ifEmpty { Int.ZERO }
        val cityId = request.cityID.ifEmpty { Int.ZERO }
        setRequestParams(
            mapOf<String, Any>(
                KEY_SHOP_ID to request.shopID.toIntOrZero(),
                KEY_PROMO_ID to request.promoID.toIntOrZero(),
                KEY_PAGE to request.page,
                KEY_PER_PAGE to request.perPage,
                KEY_SORT_BY to request.sortBy.toIntOrZero(),
                KEY_DISTRICT_ID to districtId,
                KEY_CITY_ID to cityId,
                KEY_LATITUDE to request.latitude,
                KEY_LONGITUDE to request.longitude
            )
        )
    }

    companion object {
        private const val KEY_SHOP_ID = "shopID"
        private const val KEY_PROMO_ID = "promoID"
        private const val KEY_PAGE = "page"
        private const val KEY_PER_PAGE = "perPage"
        private const val KEY_SORT_BY = "sortBy"
        private const val KEY_DISTRICT_ID = "districtID"
        private const val KEY_CITY_ID = "cityID"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
        private const val QUERY_NAME = "ShopPageMvcLockToProductQuery"
        private const val QUERY = """
            query ShopPageMvcLockToProductQuery(${'$'}shopID: Int64,${'$'}promoID: Int64!, ${'$'}page: Int, ${'$'}perPage: Int, ${'$'}sortBy: Int, ${'$'}districtID: String, ${'$'}cityID: String, ${'$'}latitude: String, ${'$'}longitude: String) {
              shopPageGetMVCLockToProduct(shopID: ${'$'}shopID, promoID: ${'$'}promoID, page: ${'$'}page, perPage: ${'$'}perPage, sortBy: ${'$'}sortBy, districtID: ${'$'}districtID, cityID: ${'$'}cityID, latitude: ${'$'}latitude, longitude: ${'$'}longitude) {
                nextPage
                voucher {
                  shopImage
                  title
                  baseCode
                  expiredWording
                  expiredTimer
                  totalQuotaLeft
                  totalQuotaLeftWording
                  minPurchaseWording
                }
                productList {
                  totalProduct
                  totalProductWording
                  data {
                    productID
                    childIDs
                    name
                    imageUrl
                    productUrl
                    displayPrice
                    originalPrice
                    discountPercentage
                    isShowFreeOngkir
                    freeOngkirPromoIcon
                    isSoldOut
                    rating
                    averageRating
                    totalReview
                    cashback
                    city
                    minimumOrder
                    labelGroups {
                      position
                      type
                      title
                      url
                    }
                    stock
                    finalPrice
                  }
                }
                error {
                  Message
                  Description
                  CtaText
                  CtaLink
                }
              }
            }
        """
    }
}