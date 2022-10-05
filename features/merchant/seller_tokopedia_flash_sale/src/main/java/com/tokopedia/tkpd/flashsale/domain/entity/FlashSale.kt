package com.tokopedia.tkpd.flashsale.domain.entity

import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleListPageTab
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import java.util.Date

data class FlashSale(
    val campaignId: Long,
    val cancellationReason: String,
    val coverImage: String,
    val description: String,
    val endDateUnix: Date,
    val maxProductSubmission: Int,
    val name: String,
    val hasEligibleProduct: Boolean,
    val productMeta: ProductMeta,
    val remainingQuota: Int,
    val reviewEndDateUnix: Date,
    val reviewStartDateUnix: Date,
    val slug: String,
    val startDateUnix: Date,
    val statusId: Int,
    val statusText: String,
    val submissionEndDateUnix: Date,
    val submissionStartDateUnix: Date,
    val useMultiLocation: Boolean,
    val formattedDate: FormattedDate,
    val status: FlashSaleStatus,
    val productCriteria: List<ProductCriteria>,
    val tabName: FlashSaleListPageTab
) {
    data class ProductMeta(
        val acceptedProduct: Int,
        val rejectedProduct: Int,
        val totalProduct: Int,
        val totalProductStock: Int,
        val totalStockSold: Int,
        val transferredProduct: Int,
        val totalSoldValue: Double
    )

    data class FormattedDate(
        val startDate: String,
        val endDate: String
    )

    data class ProductCriteria(
        val criteriaId: Long,
        val minPrice: Double,
        val maxPrice: Double,
        val minFinalPrice: Double,
        val maxFinalPrice: Double,
        val minDiscount: Int,
        val minCustomStock: Int,
        val maxCustomStock: Int,
        val minRating: Int,
        val minProductScore: Int,
        val minQuantitySold: Int,
        val maxQuantitySold: Int,
        val maxSubmission: Int,
        val maxProductAppear: Int,
        val dayPeriodTimeAppear: Int,
        val categories: List<ProductCategories>,
        val additionalInfo: AdditionalInfo
    )

    data class ProductCategories(
        val categoryId: Long,
        val categoryName: String
    )

    data class AdditionalInfo(
        val matchedProduct: Long
    )
}
