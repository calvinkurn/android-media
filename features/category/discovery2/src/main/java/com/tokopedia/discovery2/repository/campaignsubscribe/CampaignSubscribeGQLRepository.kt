package com.tokopedia.discovery2.repository.campaignsubscribe

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.campaignnotifymeresponse.CampaignNotifyMeRequest
import com.tokopedia.discovery2.data.campaignnotifymeresponse.CampaignNotifyMeResponse
import javax.inject.Inject

class CampaignSubscribeGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), CampaignSubscribeRepo {

    override suspend fun subscribeToCampaign(campaignNotifyMeRequest: CampaignNotifyMeRequest): CampaignNotifyMeResponse {
        return getGQLData(queryCampaignNotifyMeGql, CampaignNotifyMeResponse::class.java, mapOf("para" to campaignNotifyMeRequest))
    }

    private val queryCampaignNotifyMeGql: String = """mutation checkCampaignNotifyMe(${'$'}para: CheckCampaignNotifyMeRequest) {
    checkCampaignNotifyMe(params: ${'$'}para) {
      success
      message
      error_message
    }
 }""".trimIndent()
}
