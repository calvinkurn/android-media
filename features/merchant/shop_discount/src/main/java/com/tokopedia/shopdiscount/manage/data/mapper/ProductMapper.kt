package com.tokopedia.shopdiscount.manage.data.mapper

import com.tokopedia.shopdiscount.common.entity.ProductType
import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListResponse
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import java.util.*
import javax.inject.Inject

class ProductMapper @Inject constructor() {

    fun map(input: GetSlashPriceProductListResponse): List<Product> {
        return input.getSlashPriceProductList.slashPriceProductList.map { product ->
            val productType = product.find()
            Product(
                product.name,
                product.price.min,
                product.price.max,
                product.discountedPriceData.min,
                product.discountedPriceData.max,
                product.discountPercentageData.min,
                product.discountPercentageData.max,
                product.picture,
                product.stock,
                product.warehouses.size,
                0,
                Date(),
                Date(),
                productType
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
}