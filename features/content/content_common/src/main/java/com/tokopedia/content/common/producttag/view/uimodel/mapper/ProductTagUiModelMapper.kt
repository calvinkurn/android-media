package com.tokopedia.content.common.producttag.view.uimodel.mapper

import com.tokopedia.content.common.producttag.model.*
import com.tokopedia.content.common.producttag.view.uimodel.*
import com.tokopedia.filter.common.data.DynamicFilterModel
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
class ProductTagUiModelMapper @Inject constructor() {

    private val decimalFormatSymbols = DecimalFormatSymbols().apply {
        decimalSeparator = PERIOD
    }

    private val decimalFormat = DecimalFormat(DECIMAL_FORMAT, decimalFormatSymbols).apply {
        roundingMode = RoundingMode.CEILING
    }

    fun mapLastTaggedProduct(response: GetFeedLastTaggedProductResponse): PagedDataUiModel<ProductUiModel> {
        return PagedDataUiModel(
            dataList = response.data.products.map {
                ProductUiModel(
                    id = it.id,
                    shopID = it.shopID,
                    shopName = it.shopName,
                    shopBadge = listOf(ProductUiModel.ShopBadge(it.shopBadgeURL.isNotEmpty(), it.shopBadgeURL)),
                    name = it.name,
                    coverURL = it.coverURL,
                    webLink = it.webLink,
                    appLink = it.appLink,
                    star = formatStarRating(it.star),
                    price = it.price,
                    priceFmt = it.priceFmt,
                    isDiscount = it.isDiscount,
                    discount = it.discount,
                    discountFmt = it.discountFmt,
                    priceOriginal = it.priceOriginal,
                    priceOriginalFmt = it.priceOriginalFmt,
                    priceDiscount = it.priceDiscount,
                    priceDiscountFmt = it.priceDiscountFmt,
                    totalSold = it.totalSold,
                    totalSoldFmt = it.totalSoldFmt,
                    isBebasOngkir = it.isBebasOngkir,
                    bebasOngkirStatus = it.bebasOngkirStatus,
                    bebasOngkirURL = it.bebasOngkirURL,
                )
            },
            hasNextPage = response.data.nextCursor.isNotEmpty(),
            nextCursor = response.data.nextCursor,
        )
    }

    fun mapLastPurchasedProduct(response: GetFeedLastPurchaseProductResponse): LastPurchasedProductUiModel {
        return LastPurchasedProductUiModel(
            products = response.data.products.map {
                ProductUiModel(
                    id = it.id,
                    shopID = it.shopID,
                    shopName = it.shopName,
                    shopBadge = listOf(ProductUiModel.ShopBadge(it.shopBadgeURL.isNotEmpty(), it.shopBadgeURL)),
                    name = it.name,
                    coverURL = it.coverURL,
                    webLink = it.webLink,
                    appLink = it.appLink,
                    star = formatStarRating(it.star),
                    price = it.price,
                    priceFmt = it.priceFmt,
                    isDiscount = it.isDiscount,
                    discount = it.discount,
                    discountFmt = it.discountFmt,
                    priceOriginal = it.priceOriginal,
                    priceOriginalFmt = it.priceOriginalFmt,
                    priceDiscount = it.priceDiscount,
                    priceDiscountFmt = it.priceDiscountFmt,
                    totalSold = it.totalSold,
                    totalSoldFmt = it.totalSoldFmt,
                    isBebasOngkir = it.isBebasOngkir,
                    bebasOngkirStatus = it.bebasOngkirStatus,
                    bebasOngkirURL = it.bebasOngkirURL,
                )
            },
            nextCursor = response.data.nextCursor,
            state = PagedState.Success(hasNextPage = response.data.products.isNotEmpty()),
            coachmark = response.data.coachmark,
            isCoachmarkShown = response.data.isCoachmarkShown,
        )
    }

    fun mapSearchAceProducts(response: FeedAceSearchProductResponse, nextCursor: Int): PagedGlobalSearchProductResponse {
        return PagedGlobalSearchProductResponse(
            pagedData = PagedDataUiModel(
                dataList = response.wrapper.data.products.map {
                    ProductUiModel(
                        id = it.id,
                        shopID = it.shop.id,
                        shopName = it.shop.name,
                        shopBadge = it.badges.map { badge -> ProductUiModel.ShopBadge(badge.show, badge.imageUrl) },
                        name = it.name,
                        coverURL = it.imageUrl,
                        webLink = "",
                        appLink = "",
                        star = it.ratingAverage,
                        price = it.priceDouble,
                        priceFmt = it.price,
                        isDiscount = it.discountPercentage != 0.0,
                        discount = it.discountPercentage,
                        discountFmt = when (it.discountPercentage % 1.0 == 0.0) {
                            true -> "${it.discountPercentage.toString().split(".")[0]}%"
                            else -> "${it.discountPercentage}%"
                        },
                        priceOriginal = 0.0,
                        priceOriginalFmt = it.originalPrice,
                        priceDiscount = 0.0,
                        priceDiscountFmt = if (it.discountPercentage != 0.0) it.price else "",
                        totalSold = 0,
                        totalSoldFmt = it.countSold,
                        isBebasOngkir = it.freeOngkir.isActive,
                        bebasOngkirStatus = "",
                        bebasOngkirURL = it.freeOngkir.imgUrl,
                        stock = it.stock,
                    )
                },
                hasNextPage = response.wrapper.data.products.isNotEmpty(),
                nextCursor = nextCursor.toString(),
            ),
            header = SearchHeaderUiModel(
                totalData = response.wrapper.header.totalData,
                totalDataText = response.wrapper.header.totalDataText,
                responseCode = response.wrapper.header.responseCode,
                keywordProcess = response.wrapper.header.keywordProcess,
                componentId = response.wrapper.header.componentId,
            ),
            suggestion = SuggestionUiModel(
                text = response.wrapper.data.suggestion.text,
                query = response.wrapper.data.suggestion.query,
                suggestion = response.wrapper.data.suggestion.suggestion,
            ),
            ticker = TickerUiModel(
                text = response.wrapper.data.ticker.text,
                query = response.wrapper.data.ticker.query,
            )
        )
    }

    fun mapSearchAceShops(response: FeedAceSearchShopResponse, nextCursor: Int, param: SearchParamUiModel): PagedGlobalSearchShopResponse {
        return PagedGlobalSearchShopResponse(
            totalShop = response.wrapper.totalShop,
            pagedData = PagedDataUiModel(
                dataList = response.wrapper.shops.map {
                    ShopUiModel(
                        shopId = it.shopId,
                        shopName = it.shopName,
                        shopImage = it.shopImage,
                        shopLocation = it.shopLocation,
                        shopGoldShop = it.shopGoldShop,
                        shopStatus = it.shopStatus,
                        isOfficial = it.isOfficial,
                        isPMPro = it.isPMPro,
                    )
                },
                hasNextPage = response.wrapper.shops.isNotEmpty(),
                nextCursor = nextCursor.toString(),
            ),
            header = SearchHeaderUiModel(
                totalData = response.wrapper.header.totalData,
                totalDataText = response.wrapper.header.totalDataText,
                responseCode = response.wrapper.header.responseCode,
                keywordProcess = response.wrapper.header.keywordProcess,
                componentId = param.componentId,
            )
        )
    }

    fun mapQuickFilter(response: FeedQuickFilterResponse): List<QuickFilterUiModel> {
        return response.wrapper.filter
            .flatMap { it.options }
            .map {
                QuickFilterUiModel(
                    name = it.name,
                    icon = it.icon,
                    key = it.key,
                    value = it.value,
                )
            }
    }

    fun mapSortFilter(response: GetSortFilterResponse): DynamicFilterModel {
        return response.wrapper
    }

    fun mapSortFilterProductCount(response: GetSortFilterProductCountResponse): String {
        return response.wrapper.countText
    }

    fun mapShopInfo(response: GetShopInfoByIDResponse): ShopUiModel {
        val shopInfo = response.wrapper.result.firstOrNull() ?: return ShopUiModel()

        return ShopUiModel(
            shopId = shopInfo.shopCore.shopID,
            shopName = shopInfo.shopCore.name,
            shopGoldShop = shopInfo.goldOS.isGold,
            isOfficial = shopInfo.goldOS.isOfficial == 1,
            isPMPro = shopInfo.goldOS.isGoldBadge == 1,
        )
    }

    private fun formatStarRating(star: Double): String {
        return if (star == NO_STAR_RATING) ""
        else decimalFormat.format(star / STAR_RATING_DIVIDER).toString()
    }

    companion object {
        private const val DECIMAL_FORMAT = "0.0"
        private const val NO_STAR_RATING = 0.0
        private const val STAR_RATING_DIVIDER = 20.0
        private const val PERIOD = '.'
    }
}
