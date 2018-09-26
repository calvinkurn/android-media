package com.tokopedia.flashsale.management.ekstension

import com.tokopedia.flashsale.management.data.campaign_list.Campaign
import com.tokopedia.flashsale.management.view.viewmodel.CampaignViewModel

fun Campaign.toCampaignViewModel(): CampaignViewModel {
    return CampaignViewModel().also {
        it.name = this.name
        it.campaign_period = this.campaign_period
        it.status = this.status
        it.campaign_type = this.campaign_type
        it.cover = this.cover
    }
}