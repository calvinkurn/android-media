package com.tokopedia.shop.flashsale.data.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flashsale.data.response.DoSellerCampaignCreationResponse
import com.tokopedia.shop.flashsale.domain.entity.CampaignCreationResult
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