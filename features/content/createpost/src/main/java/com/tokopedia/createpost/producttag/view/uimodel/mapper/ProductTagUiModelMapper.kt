package com.tokopedia.createpost.producttag.view.uimodel.mapper

import com.tokopedia.createpost.producttag.model.FeedAceSearchProductResponse
import com.tokopedia.createpost.producttag.model.GetFeedLastPurchaseProductResponse
import com.tokopedia.createpost.producttag.model.GetFeedLastTaggedProductResponse
import com.tokopedia.createpost.producttag.view.uimodel.*
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
                    shopBadge = it.shopBadgeURL,
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
                    shopBadge = it.shopBadgeURL,
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

    fun mapMyShopProduct(response: FeedAceSearchProductResponse, nextCursor: String): PagedDataUiModel<ProductUiModel> {
        return PagedDataUiModel(
            dataList = response.wrapper.data.products.map {
                ProductUiModel(
                    id = it.id,
                    shopID = it.shop.id,
                    shopName = it.shop.name,
                    shopBadge = "", /** TODO: gonna conver this to list */
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
                )
            },
            hasNextPage = response.wrapper.data.products.isNotEmpty(),
            nextCursor = nextCursor,
        )
    }
}