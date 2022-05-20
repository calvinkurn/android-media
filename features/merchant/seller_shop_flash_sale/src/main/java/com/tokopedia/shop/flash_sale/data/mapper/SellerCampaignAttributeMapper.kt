package com.tokopedia.shop.flash_sale.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flash_sale.data.response.GetSellerCampaignAttributeResponse
import com.tokopedia.shop.flash_sale.domain.entity.CampaignAttribute
import javax.inject.Inject

class SellerCampaignAttributeMapper @Inject constructor() {

    fun map(data: GetSellerCampaignAttributeResponse): CampaignAttribute {
        val campaigns = data.getSellerCampaignAttribute.campaignDetail.map {
            CampaignAttribute.CampaignDetail(
                it.campaignId.toLongOrZero(),
                it.campaignName,
                it.endDate,
                it.startDate,
                it.statusId.toIntOrZero()
            )
        }
        val attribute = data.getSellerCampaignAttribute.shopAttribute
        val shopAttribute = CampaignAttribute.ShopAttribute(
            attribute.campaignQuota,
            attribute.maxCampaignDuration,
            attribute.maxEtalase,
            attribute.maxOverlappingCampaign,
            attribute.maxSingleProductSubmission,
            attribute.maxUpcomingDuration,
            attribute.userRelationRestriction,
            attribute.widgetBackgroundColor
        )
        return CampaignAttribute(
            data.getSellerCampaignAttribute.responseHeader.success,
            data.getSellerCampaignAttribute.responseHeader.errorMessage.joinToString { "," },
            campaigns,
            data.getSellerCampaignAttribute.maxCountAllowed,
            data.getSellerCampaignAttribute.remainingCampaignQuota,
            shopAttribute,
            data.getSellerCampaignAttribute.totalCount
        )
    }

}