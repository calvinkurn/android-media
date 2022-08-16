package com.tokopedia.tkpd.flashsale.domain.entity

import java.util.Date

data class Campaign(
    val campaignId: Long,
    val cancellationReason: String,
    val coverImage: String,
    val description: String,
    val endDateUnix: Date,
    val maxProductSubmission: Int,
    val name: String,
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
    val useMultiLocation: Boolean
) {
    data class ProductMeta(
        val acceptedProduct: Int,
        val rejectedProduct: Int,
        val totalProduct: Int,
        val totalProductStock: Int,
        val totalStockSold: Int,
        val transferredProduct: Int
    )
}
