package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.Product
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.OutOfStock
import com.tokopedia.play.view.type.StockAvailable
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import javax.inject.Inject

/**
 * Created by jegul on 01/02/21
 */
class PlayProductTagUiMapper @Inject constructor() {

    fun mapProductTag(input: Product): PlayProductUiModel {
        return PlayProductUiModel.Product(
                id = input.id.toString(),
                shopId = input.shopId,
                imageUrl = input.image,
                title = input.name,
                price = if (input.discount != 0) {
                    DiscountedPrice(
                            originalPrice = input.originalPriceFormatted,
                            discountedPrice = input.priceFormatted,
                            discountedPriceNumber = input.price,
                            discountPercent = input.discount
                    )
                } else {
                    OriginalPrice(price = input.originalPriceFormatted,
                            priceNumber = input.originalPrice)
                },
                isVariantAvailable = input.isVariant,
                stock = if (input.isAvailable) StockAvailable(input.quantity) else OutOfStock,
                minQty = input.minimumQuantity,
                isFreeShipping = input.isFreeShipping,
                applink = input.appLink
        )
    }
}