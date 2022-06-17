package com.tokopedia.shop.flashsale.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.epochToDate
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignListResponse
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignListResponse.GetSellerCampaignList.Campaign.CampaignRelationData
import com.tokopedia.shop.flashsale.domain.entity.CampaignMeta
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.shop.flashsale.domain.entity.RelatedCampaign
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_ID_CANCELLED
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_ID_DRAFT
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_ID_FINISHED
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_ID_IN_REVIEW
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_ID_IN_SUBMISSION
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_ID_ONGOING
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_ID_ONGOING_CANCELLATION
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_ID_READY
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_ID_READY_LOCKED
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_PUBLISH_CANCELLED
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_READY_CANCELLED
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_REVIEW_CANCELLED
import com.tokopedia.shop.flashsale.domain.entity.enums.CAMPAIGN_STATUS_SUBMISSION_CANCELLED
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.PAYMENT_TYPE_INSTANT
import com.tokopedia.shop.flashsale.domain.entity.enums.PAYMENT_TYPE_REGULAR
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import javax.inject.Inject

class SellerCampaignListMapper @Inject constructor() {

    fun map(data: GetSellerCampaignListResponse): CampaignMeta {
        val campaigns = data.getSellerCampaignList.campaign.map {
            CampaignUiModel(
                it.campaignId.toLongOrZero(),
                it.campaignName,
                it.endDate.epochToDate().formatTo(DateConstant.DATE),
                it.endDate.epochToDate().formatTo(DateConstant.TIME_WIB),
                it.isCancellable,
                it.isShareable,
                it.notifyMeCount,
                it.startDate.epochToDate().formatTo(DateConstant.DATE),
                it.startDate.epochToDate().formatTo(DateConstant.TIME_WIB),
                it.statusId.toIntOrZero().convert(),
                it.thematicParticipation,
                CampaignUiModel.ProductSummary(
                    it.productSummary.totalItem,
                    it.productSummary.soldItem,
                    it.productSummary.reservedProduct,
                    it.productSummary.submittedProduct,
                    it.productSummary.deletedProduct,
                    it.productSummary.visibleProductCount
                ),
                it.startDate.epochToDate(),
                it.endDate.epochToDate(),
                Gradient(it.gradientColor.firstColor, it.gradientColor.secondColor, true),
                it.useUpcomingWidget,
                it.reviewEndDate.epochToDate(),
                it.paymentType.toPaymentType(),
                it.isUniqueBuyer,
                it.isCampaignRelation,
                it.campaignRelationData.toRelatedCampaign(),
                it.isCampaignRuleSubmit,
            )
        }
        return CampaignMeta(
            data.getSellerCampaignList.totalCampaign,
            data.getSellerCampaignList.totalCampaignActive,
            data.getSellerCampaignList.totalCampaignFinished,
            campaigns
        )
    }

    private fun List<CampaignRelationData>?.toRelatedCampaign(): List<RelatedCampaign> {
        return this?.map { RelatedCampaign(it.id, it.name) } ?: emptyList()
    }

    private fun Int.convert(): CampaignStatus {
        return when (this) {
            CAMPAIGN_STATUS_ID_DRAFT -> CampaignStatus.DRAFT

            CAMPAIGN_STATUS_ID_IN_SUBMISSION -> CampaignStatus.IN_SUBMISSION
            CAMPAIGN_STATUS_ID_IN_REVIEW -> CampaignStatus.IN_REVIEW
            CAMPAIGN_STATUS_ID_READY -> CampaignStatus.READY
            CAMPAIGN_STATUS_ID_READY_LOCKED -> CampaignStatus.READY_LOCKED
            CAMPAIGN_STATUS_ID_ONGOING -> CampaignStatus.ONGOING

            CAMPAIGN_STATUS_ID_FINISHED -> CampaignStatus.FINISHED
            CAMPAIGN_STATUS_PUBLISH_CANCELLED -> CampaignStatus.PUBLISHED_CANCELLED
            CAMPAIGN_STATUS_SUBMISSION_CANCELLED -> CampaignStatus.SUBMISSION_CANCELLED
            CAMPAIGN_STATUS_REVIEW_CANCELLED -> CampaignStatus.REVIEW_CANCELLED
            CAMPAIGN_STATUS_READY_CANCELLED -> CampaignStatus.READY_CANCELLED
            CAMPAIGN_STATUS_ID_ONGOING_CANCELLATION -> CampaignStatus.ONGOING_CANCELLATION
            CAMPAIGN_STATUS_ID_CANCELLED -> CampaignStatus.CANCELLED

            else -> CampaignStatus.CANCELLED

        }
    }

    private fun Int.toPaymentType(): PaymentType {
        return when (this) {
            PAYMENT_TYPE_INSTANT -> PaymentType.INSTANT
            PAYMENT_TYPE_REGULAR -> PaymentType.REGULAR
            else -> PaymentType.INSTANT
        }
    }

}