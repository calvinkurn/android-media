package com.tokopedia.deals.pdp.ui.utils

import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.pdp.data.Brand
import com.tokopedia.deals.pdp.data.CartData
import com.tokopedia.deals.pdp.data.Catalog
import com.tokopedia.deals.pdp.data.DealRatingRequest
import com.tokopedia.deals.pdp.data.DealsRatingUpdateRequest
import com.tokopedia.deals.pdp.data.DealsRecommendMessage
import com.tokopedia.deals.pdp.data.DealsRecommendTrackingRequest
import com.tokopedia.deals.pdp.data.DealsTravelMessage
import com.tokopedia.deals.pdp.data.DealsTravelRecentSearchTrackingRequest
import com.tokopedia.deals.pdp.data.DealsVerifyRequest
import com.tokopedia.deals.pdp.data.Entertainment
import com.tokopedia.deals.pdp.data.EventVerifyResponse
import com.tokopedia.deals.pdp.data.ItemMap
import com.tokopedia.deals.pdp.data.Media
import com.tokopedia.deals.pdp.data.MetaData
import com.tokopedia.deals.pdp.data.Outlet
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.data.RecentData
import com.tokopedia.deals.pdp.data.TravelRecentSearch
import com.tokopedia.digital_deals.data.ItemMapResponse
import com.tokopedia.digital_deals.data.MetaDataResponse
import com.tokopedia.digital_deals.data.PassengerForm
import com.tokopedia.digital_deals.data.PassengerInformation
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
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

    fun mapperOldVerify(verifyResponse: EventVerifyResponse): com.tokopedia.digital_deals.data.EventVerifyResponse {
        return com.tokopedia.digital_deals.data.EventVerifyResponse(
            error = verifyResponse.error,
            errorDescription = verifyResponse.errorDescription,
            status = verifyResponse.status,
            gatewayCode = verifyResponse.gatewayCode,
            metadata = MetaDataResponse(
                categoryName = verifyResponse.metadata.categoryName,
                error = verifyResponse.metadata.error,
                itemIds = verifyResponse.metadata.itemIds,
                orderTitle = verifyResponse.metadata.orderTitle,
                orderSubTitle = verifyResponse.metadata.orderSubTitle,
                productIds = verifyResponse.metadata.productIds,
                productNames = verifyResponse.metadata.productNames,
                providerIds = verifyResponse.metadata.providerIds,
                quantity = verifyResponse.metadata.quantity,
                totalPrice = verifyResponse.metadata.totalPrice,
                itemMap = verifyResponse.metadata.itemMap.map {
                    ItemMapResponse(
                        basePrice = it.basePrice,
                        categoryId = it.categoryId,
                        childCategoryIds = it.childCategoryIds,
                        commission = it.commission,
                        commissionType = it.commissionType,
                        currencyPrice = it.currencyPrice,
                        description = it.description,
                        email = it.email,
                        endTime = it.endTime,
                        error = it.error,
                        flagId = it.flagId,
                        id = it.id,
                        invoiceId = it.invoiceId,
                        invoiceItemId = it.invoiceItemId,
                        invoiceStatus = it.invoiceStatus,
                        locationDesc = it.locationDesc,
                        locationName = it.locationName,
                        mobile = it.mobile,
                        name = it.name,
                        orderTraceId = it.orderTraceId,
                        packageId = it.packageId,
                        packageName = it.packageName,
                        paymentType = it.paymentType,
                        price = it.price,
                        productAppUrl = it.productAppUrl,
                        productId = it.productId,
                        productImage = it.productImage,
                        productName = it.productName,
                        providerId = it.providerId,
                        providerInvoiceCode = it.providerInvoiceCode,
                        providerOrderId = it.providerOrderId,
                        providerPackageId = it.providerPackageId,
                        providerScheduleId = it.providerScheduleId,
                        providerTicketId = it.providerTicketId,
                        quantity = it.quantity,
                        scheduleTimestamp = it.scheduleTimestamp,
                        startTime = it.startTime,
                        totalPrice = it.totalPrice,
                        webAppUrl = it.webAppUrl,
                        productWebUrl = it.productWebUrl,
                        passengerForms = it.passengerForms.map {
                            PassengerForm(
                                passengerInformation = it.passengerInformation.map {
                                    PassengerInformation(
                                        name = it.name,
                                        title = it.title,
                                        value = it.value
                                    )
                                }
                            )
                        }.toMutableList()
                    )
                }
            )
        )
    }

    fun mapOldProductDetailData(productDetailData: ProductDetailData): DealsDetailsResponse {
        val dealsOldProductDetailData = DealsDetailsResponse()
        dealsOldProductDetailData.apply {
            id = productDetailData.id.toIntSafely().toLong()
            brandId = productDetailData.brandId.toIntSafely().toLong()
            categoryId = productDetailData.categoryId.toIntSafely().toLong()
            providerId = productDetailData.providerId.toIntSafely().toLong()
            providerProductId = productDetailData.providerProductId
            providerProductName = productDetailData.providerProductName
            displayName = productDetailData.displayName
            url = productDetailData.url
            imageWeb = productDetailData.imageWeb
            thumbnailWeb = productDetailData.thumbnailWeb
            longRichDesc = productDetailData.longRichDesc
            mrp = productDetailData.mrp.toIntSafely().toLong()
            salesPrice = productDetailData.salesPrice.toIntSafely().toLong()
            quantity = productDetailData.quantity.toIntSafely()
            soldQuantity = productDetailData.soldQuantity.toIntSafely()
            sellRate = productDetailData.sellRate.toIntSafely()
            thumbsUp = productDetailData.thumbsUp.toIntSafely()
            thumbsDown = productDetailData.thumbsDown.toIntSafely()
            status = productDetailData.status
            minStartDate = productDetailData.minStartDate.toIntSafely()
            maxEndDate = productDetailData.maxEndDate.toIntSafely()
            saleStartDate = productDetailData.saleStartDate.toIntSafely()
            saleEndDate = productDetailData.saleEndDate.toIntSafely()
            createdAt = productDetailData.createdAt
            updatedAt = productDetailData.updatedAt
            minQty = productDetailData.minQty
            maxQty = productDetailData.maxQty
            minStartTime = productDetailData.minStartTime
            maxEndTime = productDetailData.maxEndTime
            saleStartTime = productDetailData.saleStartTime
            saleEndTime = productDetailData.saleEndTime
            dateRange = productDetailData.dateRange
            cityName = productDetailData.cityName
            outlets = mappedOutlet(productDetailData.outlets)
            rating = productDetailData.rating.toIntSafely()
            likes = productDetailData.likes
            catalog = mappedCatalog(productDetailData.catalog)
            savingPercentage = productDetailData.savingPercentage
            brand = mappedBrand(productDetailData.brand, productDetailData.brandId)
            recommendationUrl = productDetailData.recommendationUrl
            mediaUrl = mappedMediaUrl(productDetailData.media)
            tnc = productDetailData.tnc
            seoUrl = productDetailData.seoUrl
            isLiked = productDetailData.isLiked
            webUrl = productDetailData.webUrl
            appUrl = productDetailData.appUrl
            customText1 = productDetailData.customText1.toIntSafely()
            checkoutBusinessType = productDetailData.checkoutBusinessType
            checkoutDataType = productDetailData.checkoutDataType
        }
        return dealsOldProductDetailData
    }

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

    private fun mappedOutlet(outlets: List<Outlet>): List<com.tokopedia.digital_deals.view.model.Outlet> {
        val mappedOutlets = mutableListOf<com.tokopedia.digital_deals.view.model.Outlet>()
        outlets.forEach {
            val outlet = com.tokopedia.digital_deals.view.model.Outlet()
            outlet.id = it.id
            outlet.productId = it.productId
            outlet.locationId = it.locationId
            outlet.name = it.name
            outlet.searchName = it.searchName
            outlet.metaTitle = it.metaTitle
            outlet.metaDescription = it.metaDescription
            outlet.district = it.district
            outlet.gmapAddress = it.gmapAddress
            outlet.neighbourhood = it.neighbourhood
            outlet.coordinates = it.coordinates
            outlet.state = it.state
            outlet.country = it.country
            outlet.isSearchable = it.isSearchable
            outlet.locationStatus = it.locationStatus
            mappedOutlets.add(outlet)
        }

        return mappedOutlets.toList()
    }

    private fun mappedMediaUrl(media: List<Media>): List<com.tokopedia.digital_deals.view.model.Media> {
        val mappedMedias = mutableListOf<com.tokopedia.digital_deals.view.model.Media>()
        media.forEach {
            val media = com.tokopedia.digital_deals.view.model.Media()
            media.id = it.id
            media.productId = it.productId
            media.title = it.title
            media.isThumbnail = it.isThumbnail
            media.type = it.type
            media.description = it.description
            media.url = it.url
            media.client = it.client
            media.status = it.status
            mappedMedias.add(media)
        }

        return mappedMedias.toList()
    }

    private fun mappedBrand(brand: Brand, brandId: String): com.tokopedia.digital_deals.view.model.Brand {
        val mappedBrand = com.tokopedia.digital_deals.view.model.Brand()
        mappedBrand.id = brandId
        mappedBrand.title = brand.title
        mappedBrand.seoUrl = brand.seoUrl
        mappedBrand.featuredImage = brand.featuredImage
        mappedBrand.cityName = brand.cityName
        return mappedBrand
    }

    private fun mappedCatalog(catalog: Catalog): com.tokopedia.digital_deals.view.model.Catalog {
        val mappedCatalog = com.tokopedia.digital_deals.view.model.Catalog()
        mappedCatalog.digitalCategoryId = catalog.digitalCategoryId
        mappedCatalog.digitalProductId = catalog.digitalProductId
        mappedCatalog.digitalProductCode = catalog.digitalProductCode
        return mappedCatalog
    }

    private fun getDateMilis(date: Int): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, DEFAULT_LOCALE)
        val dateMilis = Date(TimeUnit.SECONDS.toMillis(date.toLong()))
        return dateFormat.format(dateMilis).toString()
    }
}
