package com.tokopedia.shop.flash_sale.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flash_sale.common.constant.DateConstant
import com.tokopedia.shop.flash_sale.common.extension.epochTo
import com.tokopedia.shop.flash_sale.common.extension.epochToDate
import com.tokopedia.shop.flash_sale.data.response.GetSellerCampaignListResponse
import com.tokopedia.shop.flash_sale.domain.entity.CampaignMeta
import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flash_sale.domain.entity.enums.*
import javax.inject.Inject

class SellerCampaignListMapper @Inject constructor() {

    fun map(data: GetSellerCampaignListResponse): CampaignMeta {
        val campaigns = data.getSellerCampaignList.campaign.map {
            val cal =   it.startDate.epochToDate()
            CampaignUiModel(
                it.campaignId.toLongOrZero(),
                it.campaignName,
                it.endDate.epochTo(DateConstant.DATE),
                it.endDate.epochTo(DateConstant.TIME_WIB),
                it.isCancellable,
                it.isShareable,
                it.notifyMeCount,
                it.startDate.epochTo(DateConstant.DATE),
                it.startDate.epochTo(DateConstant.TIME_WIB),
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
                it.startDate.epochToDate()
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