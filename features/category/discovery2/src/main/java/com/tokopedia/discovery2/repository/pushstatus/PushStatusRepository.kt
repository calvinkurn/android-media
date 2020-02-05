package com.tokopedia.discovery2.repository.pushstatus

import com.tokopedia.discovery2.data.cpmtopads.CpmTopAdsResponse
import com.tokopedia.discovery2.data.push.PushStatusResponse
import com.tokopedia.discovery2.data.push.PushSubscriptionResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper

interface PushStatusRepository {
    suspend fun checkPushStatus(compaignId:Int): PushStatusResponse
    suspend fun subscribeToPush(compaignId:Int): PushSubscriptionResponse
    suspend fun getCpmTopAdsData(paramsMobile: String): DiscoveryDataMapper.CpmTopAdsData?
}