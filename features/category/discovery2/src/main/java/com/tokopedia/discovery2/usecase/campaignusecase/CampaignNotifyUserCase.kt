package com.tokopedia.discovery2.usecase.campaignusecase

import com.tokopedia.discovery2.data.campaignnotifymeresponse.CampaignNotifyMeRequest
import com.tokopedia.discovery2.data.campaignnotifymeresponse.CampaignNotifyMeResponse
import com.tokopedia.discovery2.repository.campaignsubscribe.CampaignSubscribeRepo
import javax.inject.Inject

class CampaignNotifyUserCase @Inject constructor(private val campaignSubscribeRepo: CampaignSubscribeRepo) {
    suspend fun subscribeToCampaignNotifyMe(campaignNotifyMeRequest: CampaignNotifyMeRequest): CampaignNotifyMeResponse {
        return campaignSubscribeRepo.subscribeToCampaign(campaignNotifyMeRequest)
    }
}