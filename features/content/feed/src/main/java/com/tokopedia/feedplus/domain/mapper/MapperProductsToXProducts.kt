package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.feed.component.product.FeedTaggedProductUiModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel

/**
 * Created By : Muhammad Furqan on 12/04/23
 */
object MapperProductsToXProducts {
    fun transform(product: FeedCardProductModel): FeedTaggedProductUiModel {
        return FeedTaggedProductUiModel(
            id = product.id,
            shop = FeedTaggedProductUiModel.Shop(
                id = product.shopId,
                name = product.shopName
            ),
            appLink = product.applink,
            title = product.name,
            imageUrl = product.coverUrl,
            price = if (product.isDiscount) {
                FeedTaggedProductUiModel.DiscountedPrice(
                    discount = product.discount.toInt(),
                    originalFormattedPrice = product.priceOriginalFmt,
                    formattedPrice = product.priceDiscountFmt,
                    price = product.priceDiscount
                )
            } else {
                FeedTaggedProductUiModel.NormalPrice(
                    formattedPrice = product.priceFmt,
                    price = product.price
                )
            }
        )
    }
}
