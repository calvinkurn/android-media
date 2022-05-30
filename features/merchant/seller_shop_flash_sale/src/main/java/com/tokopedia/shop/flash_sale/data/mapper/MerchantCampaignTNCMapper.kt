package com.tokopedia.shop.flash_sale.data.mapper

import com.tokopedia.shop.flash_sale.data.response.GetMerchantCampaignTNCResponse
import com.tokopedia.shop.flash_sale.domain.entity.MerchantCampaignTNC
import javax.inject.Inject

class MerchantCampaignTNCMapper @Inject constructor() {
    fun map(data: GetMerchantCampaignTNCResponse): MerchantCampaignTNC{
        val error = MerchantCampaignTNC.Error(
            error_code = data.error.error_code,
            error_message = data.error.error_message
        )
        return MerchantCampaignTNC(
            title = data.title,
            messages = data.messages,
            error = error
        )
    }
}