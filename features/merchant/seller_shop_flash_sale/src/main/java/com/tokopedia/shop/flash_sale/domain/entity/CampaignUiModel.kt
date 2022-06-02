package com.tokopedia.shop.flash_sale.domain.entity

import com.tokopedia.shop.flash_sale.domain.entity.enums.CampaignStatus
import java.util.*

data class CampaignUiModel(
    val campaignId: Long,
    val campaignName: String,
    val endDateFormatted: String,
    val endTime: String,
    val isCancellable: Boolean,
    val isShareable: Boolean,
    val notifyMeCount: Int,
    val startDateFormatted: String,
    val startTime: String,
    val status: CampaignStatus,
    val thematicParticipation: Boolean,
    val summary: ProductSummary,
    val startDate: Date,
    val endDate: Date
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
