package com.tokopedia.discovery2.repository.pushstatus.pushstatus

import com.tokopedia.discovery2.data.push.PushStatusResponse
import com.tokopedia.discovery2.data.push.PushSubscriptionResponse
import com.tokopedia.discovery2.data.push.PushUnSubscriptionResponse

interface PushStatusRepository {
    suspend fun checkPushStatus(compaignId: Long): PushStatusResponse
    suspend fun subscribeToPush(compaignId: Long): PushSubscriptionResponse
    suspend fun unsSubscribeToPush(compaignId: Long): PushUnSubscriptionResponse
}