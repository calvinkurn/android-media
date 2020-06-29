package com.tokopedia.discovery2.repository.cpmtopads

import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper

interface CpmTopAdsRepository {
    suspend fun getCpmTopAdsData(paramsMobile: String): DiscoveryDataMapper.CpmTopAdsData?
}