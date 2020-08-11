package com.tokopedia.discovery2.repository.cpmtopads

import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.topads.sdk.domain.model.CpmModel

interface CpmTopAdsRepository {
    suspend fun getCpmTopAdsData(paramsMobile: String): CpmModel?
}