package com.tokopedia.shopdiscount.select.data.mapper

import com.tokopedia.shopdiscount.select.data.response.GetSlashPriceProductListToReserveResponse
import com.tokopedia.shopdiscount.select.domain.entity.ReservableProduct
import javax.inject.Inject

class ReservableProductMapper @Inject constructor() {

    fun map(input: GetSlashPriceProductListToReserveResponse): List<ReservableProduct> {
        return input.getSlashPriceProductListToReserve.productList.map { product ->
            ReservableProduct(
                product.productId,
                product.name,
                product.picture,
                product.price.minFormatted,
                product.price.maxFormatted,
                product.sku,
                product.stock,
                product.url,
                product.countVariant,
                product.disabled,
                product.disabledReason,
                hasSameOriginalPrice(product.price.min, product.price.max)
            )
        }

    }

    private fun hasSameOriginalPrice(originalMinPrice: Long, originalMaxPrice: Long): Boolean {
        return originalMinPrice == originalMaxPrice
    }
}