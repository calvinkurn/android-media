package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.Product
import com.tokopedia.play.data.Section
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.PlayProductSectionUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import javax.inject.Inject

/**
 * Created by jegul on 01/02/21
 */
@PlayScope
class PlayProductTagUiMapper @Inject constructor() {

    fun mapSection(input: Section): PlayProductSectionUiModel.ProductSection {
        return PlayProductSectionUiModel.ProductSection(
            title = input.sectionTitle,
            type = ProductSectionType.getSectionValue(sectionType = input.sectionType),
            productList = input.listOfProducts.map{
                mapProductTag(it, ProductSectionType.getSectionValue(sectionType = input.sectionType))
            },
            serverTime = input.serverTime,
            startTime = input.timerStartTime,
            endTime = input.timerEndTime,
            timerInfo = input.countdown.countdownInfo,
            background = input.background
        )
    }


    fun mapProductTag(input: Product, sectionType: ProductSectionType? = null): PlayProductUiModel.Product {
        return PlayProductUiModel.Product(
                id = input.id,
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
                stock = if (input.quantity > 0 && input.isAvailable) StockAvailable(input.quantity) else OutOfStock,
                minQty = input.minimumQuantity,
                isFreeShipping = input.isFreeShipping,
                applink = input.appLink,
                sectionType = sectionType
        )
    }
}