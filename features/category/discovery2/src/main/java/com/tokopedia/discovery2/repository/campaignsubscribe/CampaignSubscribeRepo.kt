package com.tokopedia.discovery2.repository.campaignsubscribe

import com.tokopedia.discovery2.data.campaignnotifymeresponse.CampaignNotifyMeRequest
import com.tokopedia.discovery2.data.campaignnotifymeresponse.CampaignNotifyMeResponse

interface CampaignSubscribeRepo {
    suspend fun subscribeToCampaign(campaignNotifyMeRequest: CampaignNotifyMeRequest): CampaignNotifyMeResponse
}
