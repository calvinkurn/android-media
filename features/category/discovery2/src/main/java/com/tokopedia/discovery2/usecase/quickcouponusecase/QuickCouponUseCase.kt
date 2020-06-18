package com.tokopedia.discovery2.usecase.quickcouponusecase

import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.cpmtopads.CpmTopAdsRepository
import javax.inject.Inject

class QuickCouponUseCase @Inject constructor(private val cpmTopAdsRepository: CpmTopAdsRepository) {

    suspend fun getCouponDetail(pageEndPoint : String) {
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