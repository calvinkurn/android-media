package com.tokopedia.tkpd.flashsale.domain.entity

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
    val status: FlashSaleStatus
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
}
