package com.tokopedia.discovery2.repository.pushstatus.pushstatus

import com.tokopedia.discovery2.data.push.PushStatusResponse
import com.tokopedia.discovery2.data.push.PushSubscriptionResponse

interface PushStatusRepository {
    suspend fun checkPushStatus(compaignId: Int): PushStatusResponse
    suspend fun subscribeToPush(compaignId: Int): PushSubscriptionResponse
}