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
            shopId = product.shopId,
            appLink = product.applink,
            title = product.name,
            imageUrl = product.coverUrl,
            price = if (product.isDiscount) {
                FeedTaggedProductUiModel.DiscountedPrice(
                    discount = product.discount.toInt(),
                    originalPrice = product.priceOriginalFmt,
                    price = product.priceFmt
                )
            } else {
                FeedTaggedProductUiModel.NormalPrice(
                    price = product.priceFmt
                )
            }
        )
    }
}
