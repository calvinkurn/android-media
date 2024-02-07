package com.tokopedia.shopdiscount.manage.data.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shopdiscount.common.entity.ProductType
import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListResponse
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountProductRuleUiModel
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountSubsidyInfoUiModel
import com.tokopedia.shopdiscount.utils.constant.DateConstant
import com.tokopedia.shopdiscount.utils.constant.DateConstant.DATE_TIME_SECOND_PRECISION_WITH_TIMEZONE
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.shopdiscount.utils.extension.toDate
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
                ),
                sku = product.sku,
                isExpand = product.isExpand,
                maxOrder = product.maxOrder.toIntOrZero(),
                isSubsidy = product.joinSubsidy,
                subsidyStatusText = product.subsidyStatusText,
                productRule = ShopDiscountProductRuleUiModel(
                    isAbleToOptOut = product.rule.isAbleToOptOut
                ),
                subsidyInfo = ShopDiscountSubsidyInfoUiModel(
                    ctaProgramLink = product.subsidyInfo.ctaProgramLink,
                    subsidyType = ShopDiscountSubsidyInfoUiModel.getSubsidyType(product.subsidyInfo.subsidyType),
                    discountedPrice = product.subsidyInfo.discountedPrice,
                    discountedPercentage = product.subsidyInfo.discountedPercentage,
                    remainingQuota = product.subsidyInfo.remainingQuota,
                    quotaSubsidy = product.subsidyInfo.quotaSubsidy,
                    maxOrder = product.subsidyInfo.maxOrder,
                    subsidyDateStart = product.subsidyInfo.subsidyDateStart,
                    subsidyDateEnd = product.subsidyInfo.subsidyDateEnd,
                    sellerDiscountPrice = product.subsidyInfo.sellerDiscountPrice,
                    sellerDiscountPercentage = product.subsidyInfo.sellerDiscountPercentage,
                    minOriginalPriceSubsidy = product.warehouses.getMinOriginalPriceSubsidy(),
                    maxOriginalPriceSubsidy = product.warehouses.getMaxOriginalPriceSubsidy(),
                    minSellerDiscountPriceSubsidy = product.warehouses.getMinSellerDiscountPriceSubsidy(),
                    maxSellerDiscountPriceSubsidy = product.warehouses.getMaxSellerDiscountPriceSubsidy(),
                    minSellerDiscountPercentageSubsidy = product.warehouses.getMinSellerDiscountPercentageSubsidy(),
                    maxSellerDiscountPercentageSubsidy = product.warehouses.getMaxSellerDiscountPercentageSubsidy(),
                    minProgramDiscountPriceSubsidy = product.warehouses.getMinProgramDiscountPriceSubsidy(),
                    maxProgramDiscountPriceSubsidy = product.warehouses.getMaxProgramDiscountPriceSubsidy(),
                    minProgramDiscountPercentageSubsidy = product.warehouses.getMinProgramDiscountPercentageSubsidy(),
                    maxProgramDiscountPercentageSubsidy = product.warehouses.getMaxProgramDiscountPercentageSubsidy(),
                    minFinalDiscountPriceSubsidy = product.warehouses.getMinFinalDiscountPriceSubsidy(),
                    maxFinalDiscountPriceSubsidy = product.warehouses.getMaxFinalDiscountPriceSubsidy(),
                    minFinalDiscountPercentageSubsidy = product.warehouses.getMinFinalDiscountPercentageSubsidy(),
                    maxFinalDiscountPercentageSubsidy = product.warehouses.getMaxFinalDiscountPercentageSubsidy(),
                ),
                listEventId = product.warehouses.map { warehouse -> warehouse.eventId },
            )
        }

    }

    private fun GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.find(): ProductType {
        val hasVariant = this.isVariant
        val availableOnMultiLocation = this.isExpand
        return when {
            !hasVariant && availableOnMultiLocation -> ProductType.SINGLE_MULTI_LOCATION
            hasVariant && availableOnMultiLocation -> ProductType.VARIANT
            hasVariant && !availableOnMultiLocation -> ProductType.VARIANT
            !hasVariant -> ProductType.SINGLE
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

    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMinOriginalPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.originalPrice.toInt()
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMaxOriginalPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.originalPrice.toInt()
        }.toMutableList().maxOrNull().orZero()
    }

    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMinSellerDiscountPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.sellerDiscountPrice.toInt()
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMaxSellerDiscountPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.sellerDiscountPrice.toInt()
        }.toMutableList().maxOrNull().orZero()
    }


    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMinSellerDiscountPercentageSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.sellerDiscountPercentage
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMaxSellerDiscountPercentageSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.sellerDiscountPercentage
        }.toMutableList().maxOrNull().orZero()
    }


    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMinProgramDiscountPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.discountedPrice.toInt()
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMaxProgramDiscountPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.discountedPrice.toInt()
        }.toMutableList().maxOrNull().orZero()
    }

    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMinProgramDiscountPercentageSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.discountedPercentage
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMaxProgramDiscountPercentageSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.subsidyInfo.discountedPercentage
        }.toMutableList().maxOrNull().orZero()
    }


    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMinFinalDiscountPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.discountedPrice.toInt()
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMaxFinalDiscountPriceSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.discountedPrice.toInt()
        }.toMutableList().maxOrNull().orZero()
    }


    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMinFinalDiscountPercentageSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.discountedPercentage
        }.toMutableList().minOrNull().orZero()
    }

    private fun List<GetSlashPriceProductListResponse.GetSlashPriceProductList.SlashPriceProduct.Warehouses>.getMaxFinalDiscountPercentageSubsidy(): Int {
        return this.filter { it.joinSubsidy }.map {
            it.discountedPercentage
        }.toMutableList().maxOrNull().orZero()
    }

}
