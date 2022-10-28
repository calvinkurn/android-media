package com.tokopedia.shop.flashsale.domain.entity

import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
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
    val useUpcomingWidget: Boolean,
    val upcomingDate: Date,
    val paymentType: PaymentType,
    val isUniqueBuyer: Boolean,
    val isCampaignRelation: Boolean,
    val relatedCampaigns: List<RelatedCampaign> = emptyList(),
    val isCampaignRuleSubmit: Boolean,
    val relativeTimeDifferenceInMinute: Long,
    val thematicInfo: ThematicInfo,
    val reviewStartDate: Date,
    val reviewEndDate: Date,
    val packageInfo: PackageInfo
) {
    data class ProductSummary(
        val totalItem: Int,
        val soldItem: Int,
        val reservedProduct: Int,
        val submittedProduct: Int,
        val deletedProduct: Int,
        val visibleProductCount: Int
    )

    data class ThematicInfo(
        val id: Long,
        val subId: Long,
        val name: String,
        val status: Long,
        val statusString: String
    )


    data class PackageInfo(
        val packageId: Long,
        val packageName: String
    )
}
