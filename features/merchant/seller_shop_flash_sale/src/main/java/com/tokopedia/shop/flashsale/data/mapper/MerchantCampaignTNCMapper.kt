package com.tokopedia.shop.flashsale.data.mapper

import com.tokopedia.shop.flashsale.data.response.GetMerchantCampaignTNCResponse
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import javax.inject.Inject

class MerchantCampaignTNCMapper @Inject constructor() {
    fun map(data: GetMerchantCampaignTNCResponse): MerchantCampaignTNC {
        val error = MerchantCampaignTNC.Error(
            errorCode = data.getMerchantCampaignTNC.error.errorCode,
            errorMessage = data.getMerchantCampaignTNC.error.errorMessage
        )
        return MerchantCampaignTNC(
            title = data.getMerchantCampaignTNC.title,
            messages = data.getMerchantCampaignTNC.messages,
            error = error
        )
    }
}