package com.tokopedia.shop.flashsale.domain.entity

import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import java.util.Date

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
    val endDate: Date,
    val gradientColor: Gradient,
    val useUpcomingWidget : Boolean
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
