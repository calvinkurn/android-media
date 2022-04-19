package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.claimCoupon.IClaimCouponGqlRepository
import javax.inject.Inject

class ClaimCouponUseCase @Inject constructor(val claimCouponRepository: IClaimCouponGqlRepository) {

    suspend fun getClickCouponData(componentId: String, pageIdentifier: String): Boolean {
        val component = getComponent(componentId, pageIdentifier)
        if (component?.noOfPagesLoaded == 1)
            return false
        component?.let { cmp ->
            cmp.setComponentsItem(claimCouponRepository.getClickCouponData(componentId, pageIdentifier), component.tabName)

            cmp.noOfPagesLoaded = 1
            return true
        }
        return false
    }
}