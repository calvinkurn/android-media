package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.repository.pushstatus.PushStatusRepository
import javax.inject.Inject

class CpmTopAdsUseCase @Inject constructor(val pushStatusRepository: PushStatusRepository) {

    suspend fun getCpmTopAdsData(paramsMobile: String): DiscoveryDataMapper.CpmTopAdsData? {
        return pushStatusRepository.getCpmTopAdsData(paramsMobile)
    }
}