package com.tokopedia.shop.flash_sale.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flash_sale.data.response.GetSellerCampaignListResponse
import com.tokopedia.shop.flash_sale.domain.entity.Campaign
import com.tokopedia.shop.flash_sale.domain.entity.CampaignMeta
import javax.inject.Inject

class SellerCampaignListMapper @Inject constructor() {

    fun map(data: GetSellerCampaignListResponse): CampaignMeta {
        val campaigns = data.getSellerCampaignList.campaign.map {
            Campaign(
                it.bitmaskIsSet,
                it.campaignId.toLongOrZero(),
                it.campaignName,
                it.campaignTypeId.toIntOrZero(),
                it.campaignTypeName,
                it.coverImg,
                it.endDate,
                it.etalasePrefix,
                it.finishedWidgetTime,
                it.finishedWidgetTimeInMins,
                it.isCampaignRelation,
                it.isCampaignRuleSubmit,
                it.isCancellable,
                it.isShareable,
                it.isUniqueBuyer,
                it.maxProductSubmission,
                it.notifyMeCount,
                it.paymentType,
                it.redirectUrl,
                it.redirectUrlApp,
                it.reviewEndDate,
                it.reviewStartDate,
                it.startDate,
                it.statusDetail,
                it.statusId.toIntOrZero(),
                it.statusText,
                it.submissionEndDate,
                it.submissionStartDate,
                it.thematicParticipation,
                it.useUpcomingWidget
            )
        }
        return CampaignMeta(
            data.getSellerCampaignList.totalCampaign,
            data.getSellerCampaignList.totalCampaignActive,
            data.getSellerCampaignList.totalCampaignFinished,
            campaigns
        )
    }

}