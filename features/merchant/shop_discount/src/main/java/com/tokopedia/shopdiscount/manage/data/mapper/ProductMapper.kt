package com.tokopedia.shopdiscount.manage.data.mapper

import com.tokopedia.shopdiscount.common.entity.ProductType
import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListResponse
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.constant.DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.utils.date.toDate
import javax.inject.Inject

class ProductMapper @Inject constructor() {

    companion object {
        private const val END_DATE_FORMAT = "dd MMM yyyy HH:mm z"
    }

    fun map(input: GetSlashPriceProductListResponse): List<Product> {
        return input.getSlashPriceProductList.slashPriceProductList.map { product ->
            val productType = product.find()
            Product(
                product.productId,
                product.name,
                product.price.minFormatted,
                product.price.maxFormatted,
                product.discountedPriceData.minFormatted,
                product.discountedPriceData.maxFormatted,
                product.discountPercentageData.minFormatted,
                product.discountPercentageData.maxFormatted,
                product.picture,
                product.stock,
                product.warehouses.size,
                product.isVariant,
                product.startDate.formatStartDate(),
                product.endDate.formatEndDate(),
                productType,
                hasSameDiscountPercentageAmount(
                    product.discountPercentageData.min,
                    product.discountPercentageData.max
                ),
                hasSameDiscountedPriceAmount(
                    product.discountedPriceData.min,
                    product.discountedPriceData.max
                ),
                hasSameOriginalPrice(
                    product.price.min,
                    product.price.max
                )
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

    private fun String.formatStartDate(): String {
        return this.toDate(DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE)
            .parseTo(DateConstant.DATE_TIME_MINUTE_PRECISION)
    }

    private fun String.formatEndDate(): String {
        return this.toDate(DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE)
            .parseTo(END_DATE_FORMAT)
    }

    private fun hasSameDiscountPercentageAmount(
        minDiscountPercentage: Int,
        maxDiscountPercentage: Int
    ): Boolean {
        return minDiscountPercentage == maxDiscountPercentage
    }

    private fun hasSameDiscountedPriceAmount(
        minDiscountedPrice: Long,
        maxDiscountedPrice: Long
    ): Boolean {
        return minDiscountedPrice == maxDiscountedPrice
    }

    private fun hasSameOriginalPrice(originalMinPrice: Long, originalMaxPrice: Long): Boolean {
        return originalMinPrice == originalMaxPrice
    }
}