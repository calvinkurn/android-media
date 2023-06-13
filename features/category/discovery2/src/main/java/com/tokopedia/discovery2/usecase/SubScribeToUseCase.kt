package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.push.PushSubscriptionResponse
import com.tokopedia.discovery2.data.push.PushUnSubscriptionResponse
import com.tokopedia.discovery2.repository.pushstatus.pushstatus.PushStatusRepository
import javax.inject.Inject

class SubScribeToUseCase @Inject constructor(val pushStatusRepository: PushStatusRepository) {

    suspend fun subscribeToPush(campaignId: Long): PushSubscriptionResponse {
        return pushStatusRepository.subscribeToPush(campaignId)
    }

    suspend fun unSubscribeToPush(campaignId: Long): PushUnSubscriptionResponse {
        return pushStatusRepository.unsSubscribeToPush(campaignId)
    }
}