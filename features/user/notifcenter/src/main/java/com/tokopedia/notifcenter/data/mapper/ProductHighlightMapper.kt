package com.tokopedia.notifcenter.data.mapper

import com.tokopedia.notifcenter.data.entity.Campaign
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.entity.ProductHighlightItem
import com.tokopedia.notifcenter.data.viewbean.ProductHighlightViewBean

object ProductHighlightMapper {

    fun map(element: ProductHighlightItem): List<ProductHighlightViewBean> {
        return element.aceProduct.data.products.map {
            ProductHighlightViewBean(
                    id = it.id,
                    name = it.name,
                    imageUrl = it.imageURL,
                    price = it.price,
                    priceInt = it.priceInt.toInt(),
                    isStockEmpty = it.isStockEmpty,
                    freeOngkirIcon = it.freeOngkir?.imgURL.toString(),
                    isFreeOngkir = it.freeOngkir?.isActive?: false,
                    discountPercentage = it.discountPercentage,
                    originalPrice = it.originalPrice,
                    shop = it.shop
            )
        }.toList()
    }

    fun mapToCampaign(element: ProductHighlightViewBean): Campaign {
        return Campaign(
                active = element.discountPercentage != 0,
                originalPriceFormat = element.originalPrice,
                discountPercentage = element.discountPercentage
        )
    }

    fun mapToProductData(element: ProductHighlightViewBean): ProductData {
        return ProductData(
                productId = element.id.toString(),
                shop = element.shop,
                price = element.priceInt.toString(),
                priceFormat = element.price,
                name = element.name
        )
    }

}