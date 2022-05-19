package com.tokopedia.createpost.producttag.view.uimodel.mapper

import com.tokopedia.createpost.producttag.model.*
import com.tokopedia.createpost.producttag.view.uimodel.*
import com.tokopedia.filter.common.data.DynamicFilterModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
class ProductTagUiModelMapper @Inject constructor() {

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
                    star = it.star,
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
                    star = it.star,
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
                        price = it.priceInt.toDouble(),
                        priceFmt = it.price,
                        isDiscount = it.discountPercentage != 0.0,
                        discount = it.discountPercentage,
                        discountFmt = "${it.discountPercentage}%",
                        priceOriginal = 0.0,
                        priceOriginalFmt = it.originalPrice,
                        priceDiscount = 0.0,
                        priceDiscountFmt = "",
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
            suggestion = response.wrapper.data.suggestion.text,
            ticker = TickerUiModel(
                text = response.wrapper.data.ticker.text,
                query = response.wrapper.data.ticker.query,
            )
        )
    }

    fun mapSearchAceShops(response: FeedAceSearchShopResponse, nextCursor: Int): PagedGlobalSearchShopResponse {
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
}