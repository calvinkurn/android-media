package com.tokopedia.flashsale.management.ekstension

import com.tokopedia.flashsale.management.data.campaignlabel.CampaignStatus
import com.tokopedia.flashsale.management.data.campaignlabel.DataCampaignLabel
import com.tokopedia.flashsale.management.data.campaignlist.Campaign
import com.tokopedia.flashsale.management.view.viewmodel.CampaignStatusViewModel
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

fun CampaignStatus.toCampaignStatusViewModel(): CampaignStatusViewModel {
    return CampaignStatusViewModel().also {
        it.label_name = this.label_name
        it.status_id = this.status_id
    }
}