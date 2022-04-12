package com.tokopedia.shopdiscount.select.data.mapper

import com.tokopedia.shopdiscount.common.entity.ProductType
import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListResponse
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

    private fun GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.find(): ProductType {
        val hasVariant = this.isVariant
        val availableOnMultiLocation = this.warehouses.isNotEmpty()
        return when {
            !hasVariant -> ProductType.SINGLE
            !hasVariant && availableOnMultiLocation -> ProductType.SINGLE_MULTI_LOCATION
            hasVariant -> ProductType.VARIANT
            hasVariant && availableOnMultiLocation -> ProductType.VARIANT_MULTI_LOCATION
            else -> ProductType.SINGLE
        }
    }

    private fun hasSameOriginalPrice(originalMinPrice: Long, originalMaxPrice: Long): Boolean {
        return originalMinPrice == originalMaxPrice
    }
}