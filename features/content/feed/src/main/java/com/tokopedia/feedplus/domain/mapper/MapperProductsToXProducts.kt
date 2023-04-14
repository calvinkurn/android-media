package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel

/**
 * Created By : Muhammad Furqan on 12/04/23
 */
object MapperProductsToXProducts {
    fun transform(products: List<FeedCardProductModel>): List<FeedXProduct> =
        products.map {
            FeedXProduct(
                appLink = it.applink,
                bebasOngkirStatus = it.bebasOngkirStatus,
                bebasOngkirURL = it.bebasOngkirUrl,
                coverURL = it.coverUrl,
                discount = it.discount.toInt(),
                discountFmt = it.discountFmt,
                isCashback = it.isCashback,
                cashbackFmt = it.cashbackFmt,
                id = it.id,
                isBebasOngkir = it.isBebasOngkir,
                isDiscount = it.isDiscount,
                name = it.name,
                price = it.price.toInt(),
                priceDiscount = it.priceDiscount.toInt(),
                priceDiscountFmt = it.priceDiscountFmt,
                priceFmt = it.priceFmt,
                priceOriginal = it.priceOriginal.toInt(),
                priceOriginalFmt = it.priceOriginalFmt,
                star = it.star.toInt(),
                totalSold = it.totalSold,
                webLink = it.weblink,
                priceMasked = it.priceMasked.toFloat(),
                priceMaskedFmt = it.priceMaskedFmt,
                stockWording = it.stockWording,
                stockSoldPercentage = it.stockSoldPercentage,
                cartable = it.cartable,
                shopID = it.shopId,
                shopName = it.shopName,
            )
        }
}
