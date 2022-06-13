package com.tokopedia.shop.flashsale.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.common.extension.epochToDate
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignListResponse
import com.tokopedia.shop.flashsale.domain.entity.CampaignMeta
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.enums.*
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
                it.endDate.epochToDate()
            )
        }
        return CampaignMeta(
            data.getSellerCampaignList.totalCampaign,
            data.getSellerCampaignList.totalCampaignActive,
            data.getSellerCampaignList.totalCampaignFinished,
            campaigns
        )
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

}