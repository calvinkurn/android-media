package com.tokopedia.deals.ui.pdp.ui.utils

import com.tokopedia.common_entertainment.data.CartData
import com.tokopedia.common_entertainment.data.DealsVerifyRequest
import com.tokopedia.common_entertainment.data.ItemMap
import com.tokopedia.common_entertainment.data.MetaData
import com.tokopedia.deals.utils.DealsUtils
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DealsPDPMapper {

    private const val CATEGORY_NAME = "deal"
    private const val DATE_FORMAT = " dd MMM yyyy hh:mm"
    private val DEFAULT_LOCALE = Locale("in", "ID")
    private const val ACTION_TRACKING_RECOMMENDATION = "product-detail"
    private const val SERVICE_TRACKING_RECOMMENDATION = "Recommendation_For_You"
    private const val SERVICE_TRACKING_RECENT_SEARCH = "travel_recent_search"
    private const val NSQ_USE_CASE = "24"
    private const val DEALS_DATA_TYPE = "deal"

    fun mapVerifyRequest(currentQuantity: Int, dealsResponse: com.tokopedia.deals.ui.pdp.data.ProductDetailData): DealsVerifyRequest {
        return DealsVerifyRequest(
            book = true,
            checkout = false,
            cartdata = CartData(
                metadata = MetaData(
                    categoryName = CATEGORY_NAME,
                    totalPrice = currentQuantity * dealsResponse.salesPrice.toIntSafely().toLong(),
                    quantity = currentQuantity,
                    productIds = listOf(dealsResponse.id),
                    productNames = listOf(dealsResponse.displayName),
                    providerIds = listOf(dealsResponse.providerId),
                    itemIds = listOf(dealsResponse.id),
                    itemMaps = listOf(
                        ItemMap(
                            id = dealsResponse.id,
                            name = dealsResponse.displayName,
                            productId = dealsResponse.id,
                            productName = dealsResponse.displayName,
                            providerId = dealsResponse.providerId,
                            categoryId = dealsResponse.categoryId,
                            startTime = getDateMilis(dealsResponse.minStartDate.toIntSafely()),
                            endTime = getDateMilis(dealsResponse.maxEndDate.toIntSafely()),
                            price = dealsResponse.salesPrice.toDouble(),
                            quantity = currentQuantity,
                            totalPrice = currentQuantity * dealsResponse.salesPrice.toIntSafely().toLong(),
                            scheduleTimestamp = dealsResponse.maxEndDate,
                            productImage = dealsResponse.imageWeb,
                            flagID = dealsResponse.customText1
                        )
                    )
                )
            )
        )
    }

    fun mapperParamUpdateRating(productId: String, userId: String, isLiked: Boolean): com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateRequest {
        return com.tokopedia.deals.ui.pdp.data.DealsRatingUpdateRequest(
            com.tokopedia.deals.ui.pdp.data.DealRatingRequest(
                feedback = "",
                isLiked = isLiked.toString(),
                productId = productId.toIntSafely().toLong(),
                rating = Int.ZERO,
                userId = userId.toIntSafely().toLong()
            )
        )
    }

    fun mapperParamTrackingRecommendation(productId: String, userId: String): com.tokopedia.deals.ui.pdp.data.DealsRecommendTrackingRequest {
        return com.tokopedia.deals.ui.pdp.data.DealsRecommendTrackingRequest(
            message = com.tokopedia.deals.ui.pdp.data.DealsRecommendMessage(
                action = ACTION_TRACKING_RECOMMENDATION,
                productId = productId.toIntSafely().toLong(),
                useCase = NSQ_USE_CASE,
                userId = userId.toIntSafely().toLong()
            ),
            service = SERVICE_TRACKING_RECOMMENDATION
        )
    }

    fun mapperParamTrackingRecentSearch(productDetailData: com.tokopedia.deals.ui.pdp.data.ProductDetailData, userId: String): com.tokopedia.deals.ui.pdp.data.DealsTravelRecentSearchTrackingRequest {
        return com.tokopedia.deals.ui.pdp.data.DealsTravelRecentSearchTrackingRequest(
            message = com.tokopedia.deals.ui.pdp.data.DealsTravelMessage(
                userId = userId.toIntSafely().toLong()
            ),
            service = SERVICE_TRACKING_RECENT_SEARCH,
            travelRecentSearch = com.tokopedia.deals.ui.pdp.data.TravelRecentSearch(
                dataType = DEALS_DATA_TYPE,
                recentData = com.tokopedia.deals.ui.pdp.data.RecentData(
                    entertainment = com.tokopedia.deals.ui.pdp.data.Entertainment(
                        appUrl = productDetailData.appUrl,
                        value = productDetailData.displayName,
                        price = DealsUtils.convertToCurrencyString(
                            productDetailData.salesPrice.toIntSafely().toLong()
                        ),
                        imageUrl = if (productDetailData.media.isNotEmpty()) productDetailData.media.first().url else "",
                        id = productDetailData.brand.title,
                        pricePrefix = DealsUtils.convertToCurrencyString(
                            productDetailData.mrp.toIntSafely().toLong()
                        ),
                        url = productDetailData.webUrl
                    )
                )
            )
        )
    }

    fun productImagesMapper(productDetail: com.tokopedia.deals.ui.pdp.data.ProductDetailData): List<String> {
        val images = mutableListOf<String>()
        if (productDetail.media.isNotEmpty()) {
            images.addAll(
                productDetail.media.map {
                    it.url
                }
            )
        } else {
            images.add(productDetail.imageApp)
        }
        return images
    }

    private fun getDateMilis(date: Int): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, DEFAULT_LOCALE)
        val dateMilis = Date(TimeUnit.SECONDS.toMillis(date.toLong()))
        return dateFormat.format(dateMilis).toString()
    }
}
