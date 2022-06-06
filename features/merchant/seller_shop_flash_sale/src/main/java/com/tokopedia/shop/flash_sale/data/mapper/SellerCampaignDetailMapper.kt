package com.tokopedia.shop.flash_sale.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flash_sale.common.constant.DateConstant
import com.tokopedia.shop.flash_sale.common.extension.epochToDate
import com.tokopedia.shop.flash_sale.common.extension.formatTo
import com.tokopedia.shop.flash_sale.data.response.GetSellerCampaignListResponse
import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flash_sale.domain.entity.enums.CAMPAIGN_STATUS_ID_AVAILABLE
import com.tokopedia.shop.flash_sale.domain.entity.enums.CAMPAIGN_STATUS_ID_CANCELLED
import com.tokopedia.shop.flash_sale.domain.entity.enums.CAMPAIGN_STATUS_ID_DRAFT
import com.tokopedia.shop.flash_sale.domain.entity.enums.CAMPAIGN_STATUS_ID_FINISHED
import com.tokopedia.shop.flash_sale.domain.entity.enums.CAMPAIGN_STATUS_ID_ONGOING
import com.tokopedia.shop.flash_sale.domain.entity.enums.CAMPAIGN_STATUS_ID_UPCOMING
import com.tokopedia.shop.flash_sale.domain.entity.enums.CampaignStatus
import javax.inject.Inject

class SellerCampaignDetailMapper @Inject constructor() {
    fun map(data: GetSellerCampaignListResponse): CampaignUiModel {
        val campaign = data.getSellerCampaignList.campaign.first()
        return with(campaign) {
            CampaignUiModel(
                campaignId.toLongOrZero(),
                campaignName,
                endDate.epochToDate().formatTo(DateConstant.DATE),
                endDate.epochToDate().formatTo(DateConstant.TIME_WIB),
                isCancellable,
                isShareable,
                notifyMeCount,
                startDate.epochToDate().formatTo(DateConstant.DATE),
                startDate.epochToDate().formatTo(DateConstant.TIME_WIB),
                statusId.toIntOrZero().convert(),
                thematicParticipation,
                CampaignUiModel.ProductSummary(
                    productSummary.totalItem,
                    productSummary.soldItem,
                    productSummary.reservedProduct,
                    productSummary.submittedProduct,
                    productSummary.deletedProduct,
                    productSummary.visibleProductCount
                ),
                startDate.epochToDate(),
                endDate.epochToDate(),
            )
        }
    }

    private fun Int.convert(): CampaignStatus {
        return when (this) {
            CAMPAIGN_STATUS_ID_AVAILABLE -> CampaignStatus.AVAILABLE
            CAMPAIGN_STATUS_ID_UPCOMING -> CampaignStatus.UPCOMING
            CAMPAIGN_STATUS_ID_ONGOING -> CampaignStatus.ONGOING
            CAMPAIGN_STATUS_ID_FINISHED -> CampaignStatus.FINISHED
            CAMPAIGN_STATUS_ID_CANCELLED -> CampaignStatus.CANCELLED
            CAMPAIGN_STATUS_ID_DRAFT -> CampaignStatus.DRAFT
            else -> CampaignStatus.AVAILABLE
        }
    }

}