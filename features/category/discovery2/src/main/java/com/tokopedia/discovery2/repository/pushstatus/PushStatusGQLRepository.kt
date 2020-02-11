package com.tokopedia.discovery2.repository.pushstatus

import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.cpmtopads.CpmTopAdsResponse
import com.tokopedia.discovery2.data.push.PushStatusResponse
import com.tokopedia.discovery2.data.push.PushSubscriptionResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.tradein_common.repository.BaseRepository
import javax.inject.Inject


class PushStatusGQLRepository @Inject constructor(val getGQLString: (Int) -> String) : BaseRepository(), PushStatusRepository {

    override suspend fun checkPushStatus(compaignId: Int): PushStatusResponse {
        return getGQLData(getGQLString(R.raw.check_push_reminder_gql),
                PushStatusResponse::class.java, mapOf("campaignID" to compaignId)) as PushStatusResponse
    }

    override suspend fun subscribeToPush(compaignId: Int): PushSubscriptionResponse {
        return getGQLData(getGQLString(R.raw.set_push_reminder_gql),
                PushSubscriptionResponse::class.java, mapOf("campaignID" to compaignId)) as PushSubscriptionResponse
    }




}