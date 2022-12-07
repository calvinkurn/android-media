package com.tokopedia.deals.pdp.ui.utils

import com.tokopedia.common_entertainment.data.CartData
import com.tokopedia.common_entertainment.data.DealsVerifyRequest
import com.tokopedia.common_entertainment.data.ItemMap
import com.tokopedia.common_entertainment.data.MetaData
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.pdp.data.DealRatingRequest
import com.tokopedia.deals.pdp.data.DealsRatingUpdateRequest
import com.tokopedia.deals.pdp.data.DealsRecommendMessage
import com.tokopedia.deals.pdp.data.DealsRecommendTrackingRequest
import com.tokopedia.deals.pdp.data.DealsTravelMessage
import com.tokopedia.deals.pdp.data.DealsTravelRecentSearchTrackingRequest
import com.tokopedia.deals.pdp.data.Entertainment
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.data.RecentData
import com.tokopedia.deals.pdp.data.TravelRecentSearch
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
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

    fun mapVerifyRequest(currentQuantity: Int, dealsResponse: ProductDetailData): DealsVerifyRequest {
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

    fun mapperParamUpdateRating(productId: String, userId: String, isLiked: Boolean): DealsRatingUpdateRequest {
        return DealsRatingUpdateRequest(
            DealRatingRequest(
                feedback = "",
                isLiked = isLiked.toString(),
                productId = productId.toIntSafely().toLong(),
                rating = Int.ZERO,
                userId = userId.toIntSafely().toLong()
            )
        )
    }

    fun mapperParamTrackingRecommendation(productId: String, userId: String): DealsRecommendTrackingRequest {
        return DealsRecommendTrackingRequest(
            message = DealsRecommendMessage(
                action = ACTION_TRACKING_RECOMMENDATION,
                productId = productId.toIntSafely().toLong(),
                useCase = NSQ_USE_CASE,
                userId = userId.toIntSafely().toLong()
            ),
            service = SERVICE_TRACKING_RECOMMENDATION
        )
    }

    fun mapperParamTrackingRecentSearch(productDetailData: ProductDetailData, userId: String): DealsTravelRecentSearchTrackingRequest {
        return DealsTravelRecentSearchTrackingRequest(
            message = DealsTravelMessage(
                userId = userId.toIntSafely().toLong()
            ),
            service = SERVICE_TRACKING_RECENT_SEARCH,
            travelRecentSearch = TravelRecentSearch(
                dataType = DEALS_DATA_TYPE,
                recentData = RecentData(
                    entertainment = Entertainment(
                        appUrl = productDetailData.appUrl,
                        value = productDetailData.displayName,
                        price = DealsUtils.convertToCurrencyString(productDetailData.salesPrice.toIntSafely().toLong()),
                        imageUrl = if (productDetailData.media.isNotEmpty()) productDetailData.media.first().url else "",
                        id = productDetailData.brand.title,
                        pricePrefix = DealsUtils.convertToCurrencyString(productDetailData.mrp.toIntSafely().toLong()),
                        url = productDetailData.webUrl
                    )
                )
            )
        )
    }

    fun productImagesMapper(productDetail: ProductDetailData): List<String> {
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
