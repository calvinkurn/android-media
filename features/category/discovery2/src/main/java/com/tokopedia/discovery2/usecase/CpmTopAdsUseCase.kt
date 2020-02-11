package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.repository.cpmtopads.CpmTopAdsRepository
import javax.inject.Inject

class CpmTopAdsUseCase @Inject constructor(private val cpmTopAdsRepository: CpmTopAdsRepository) {

    suspend fun getCpmTopAdsData(paramsMobile: String): DiscoveryDataMapper.CpmTopAdsData? {
        return cpmTopAdsRepository.getCpmTopAdsData(paramsMobile)
    }
}