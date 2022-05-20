package com.tokopedia.shop.flash_sale.data.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flash_sale.data.response.DoSellerCampaignCreationResponse
import com.tokopedia.shop.flash_sale.domain.entity.CampaignCreationResult
import javax.inject.Inject

class DoSellerCampaignCreationMapper @Inject constructor() {

    fun map(response: DoSellerCampaignCreationResponse): CampaignCreationResult {
        return CampaignCreationResult(
            response.campaignId.toLongOrZero(),
            response.isSuccess,
            response.totalProductFailed,
            response.sellerCampaignErrorCreation.errorDescription,
            response.sellerCampaignErrorCreation.errorTitle
        )

    }

}