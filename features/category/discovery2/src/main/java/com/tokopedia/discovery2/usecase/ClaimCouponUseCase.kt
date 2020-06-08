package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.claimCoupon.IClaimCouponRepository
import javax.inject.Inject

class ClaimCouponUseCase @Inject constructor(val claimCouponRepository: IClaimCouponRepository) {

    suspend fun getClickCouponData(componentId: String, pageIdentifier: String): Boolean {
        val component = getComponent(componentId, pageIdentifier)
        if (component?.noOfPagesLoaded == 1)
            return false
        component?.let { cmp ->
            cmp.componentsItem = claimCouponRepository.getClickCouponData(GenerateUrl.getClaimCouponUrl(pageIdentifier, componentId))

            cmp.noOfPagesLoaded = 1
            return true
        }
        return false
    }
}