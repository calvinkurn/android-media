package com.tokopedia.shop.flash_sale.domain.entity

data class CampaignUiModel(
    val campaignId: Long,
    val campaignName: String,
    val endDate: String,
    val endTime: String,
    val isCancellable: Boolean,
    val isShareable: Boolean,
    val notifyMeCount: Int,
    val startDate: String,
    val startTime: String,
    val statusId: Int,
    val thematicParticipation: Boolean,
    val summary: ProductSummary
) {
    data class ProductSummary(
        val totalItem: Int,
        val soldItem: Int,
        val reservedProduct: Int,
        val submittedProduct: Int,
        val deletedProduct: Int,
        val visibleProductCount: Int
    )
}
