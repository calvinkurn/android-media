package com.tokopedia.tkpd.flashsale.domain.entity

data class Campaign(
    val campaignId: Long,
    val cancellationReason: String,
    val coverImage: String,
    val description: String,
    val endDateUnix: Long,
    val maxProductSubmission: Int,
    val name: String,
    val hasEligibleProduct: Boolean,
    val productMeta: ProductMeta,
    val remainingQuota: Int,
    val reviewEndDateUnix: Long,
    val reviewStartDateUnix: Long,
    val slug: String,
    val startDateUnix: Long,
    val statusId: Int,
    val statusText: String,
    val submissionEndDateUnix: Long,
    val submissionStartDateUnix: Long,
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
