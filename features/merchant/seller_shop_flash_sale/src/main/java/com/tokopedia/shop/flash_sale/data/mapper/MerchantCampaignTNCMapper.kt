package com.tokopedia.shop.flash_sale.data.mapper

import com.tokopedia.shop.flash_sale.data.response.GetMerchantCampaignTNCResponse
import com.tokopedia.shop.flash_sale.domain.entity.MerchantCampaignTNC
import javax.inject.Inject

class MerchantCampaignTNCMapper @Inject constructor() {
    fun map(data: GetMerchantCampaignTNCResponse): MerchantCampaignTNC{
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