package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.cpmtopads.CpmTopAdsRepository
import javax.inject.Inject

class CpmTopAdsUseCase @Inject constructor(private val cpmTopAdsRepository: CpmTopAdsRepository) {

    suspend fun getCpmTopAdsData(componentId: String, pageIdentifier: String): Boolean {
        val component = getComponent(componentId, pageIdentifier)
        if (component?.noOfPagesLoaded == 1)
            return false
        component?.let { cmp ->
            component.cpmData = component.data?.get(0)?.paramsMobile?.let { cpmTopAdsRepository.getCpmTopAdsData(it) }
            cmp.noOfPagesLoaded = 1
            return true
        }
        return false
    }
}