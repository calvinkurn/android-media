package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.data.push.PushStatusResponse
import com.tokopedia.discovery2.repository.pushstatus.pushstatus.PushStatusRepository
import javax.inject.Inject


class CheckPushStatusUseCase @Inject constructor(val pushStatusRepository: PushStatusRepository) {

    suspend fun checkPushStatus(campaignId: Long): PushStatusResponse {
        return pushStatusRepository.checkPushStatus(campaignId)
    }
}